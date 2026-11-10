# SwasthyaQueue

A queue and triage system for busy hospitals, the kind of government or public hospital where hundreds of people turn up and nobody really knows how long they'll wait.

I built this to learn backend engineering properly. Instead of a to-do app, I picked a problem with real design questions: who should be seen first, what happens when two staff click the same button at once, and how one system can serve many hospitals without mixing their data up.

It is a learning project. The priority formula is my own reasoning, not clinical advice, and it should not be used for real patients.

## What it does

**Priority queue.** A patient's priority is not just first come, first served. A critical patient should not wait behind twenty minor cases, but a minor case should not wait forever either. So the score is:

```
priority = severity x 10 + minutes waited x 0.3
```

Severity decides most of it, and waiting time slowly pushes a patient up (this is called "aging"). I picked the weights by testing numbers against an edge case. My first guess (3 and 2) let a mild patient who waited 10 minutes overtake a critical one who had just arrived, so I changed it.

**Pre-checkup router.** Some departments need another one done first, for example a blood test before consultation. Each rule is a row in a `DepartmentPrerequisite` table, and a token cannot be created until the patient has a completed token in every prerequisite department.

**Wait time and alerts.** Estimated wait is the number of people ahead times the department's average time per patient. If a department has no average set, it falls back to 10 minutes. When the wait is 15 minutes or less, the token gets an alert flag.

**Many hospitals, one app.** Every table has a `hospital_id`. The hospital comes from the signed login token, never from the request body, so staff can only see and change their own hospital's data.

**Roles.** `ADMIN` sets up departments, average times, prerequisites and staff accounts. `STAFF` registers patients, checks them in and completes tokens. The live queue page is public, so a waiting-room screen needs no login.

## Tech stack

| Part | What I used |
|------|-------------|
| Backend | Java 17, Spring Boot, Spring Data JPA (Hibernate), Spring Security |
| Auth | JWT (jjwt), BCrypt password hashing |
| Database | PostgreSQL |
| Cache | Redis |
| Frontend | Angular (standalone components, signals, reactive forms), SCSS |
| Tests | JUnit 5, Mockito |
| Other | Maven wrapper, Docker for Postgres and Redis |

## How it fits together

```
Angular  ->  Controller  ->  Service  ->  Repository  ->  PostgreSQL
                                |
                                +-> Redis (live priority scores)
```

- Controllers only translate HTTP to a Service call. No business logic.
- Services hold the rules (duplicate checks, prerequisites, priority, wait times).
- Responses are DTOs, never entities. I found out why the hard way (see below).
- Live priority scores sit in Redis. Writing a score to Postgres for every waiting patient each time someone looked at the queue was wasteful, so a background job copies them to Postgres every 30 seconds instead.

```
backend/src/main/java/com/swasthyaqueue/backend/
  config/  controller/  dto/  entity/  exception/
  repository/  security/  service/
frontend/src/app/
  pages/  services/  guards/  interceptors/  layout/
docs/
```

## API

| Method | Endpoint | Who |
|--------|----------|-----|
| POST | `/api/staff/login` | public |
| POST | `/api/staff` | ADMIN |
| GET | `/api/departments` | STAFF, ADMIN |
| POST | `/api/departments` | ADMIN |
| PUT | `/api/departments/{id}/avg-time` | ADMIN |
| POST | `/api/departments/{id}/prerequisites/{prerequisiteId}` | ADMIN |
| POST | `/api/patients` | STAFF |
| GET | `/api/patients`, `/api/patients/{id}` | STAFF |
| POST | `/api/queue-tokens` | STAFF |
| PUT | `/api/queue-tokens/token/{id}` | STAFF |
| GET | `/api/queue-tokens/department/{id}` | public |
| GET | `/api/queue-tokens/department/{id}/all` | public |
| GET | `/api/dashboard` | STAFF, ADMIN |

## Running it locally

You need Java 17, Node 20+, and Docker.

**1. Start Postgres and Redis**

```bash
docker run -d --name swasthyaqueue-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16
docker run -d --name swasthyaqueue-redis -p 6379:6379 redis
```

**2. Create the database and the token-number sequence**

```bash
docker exec -it swasthyaqueue-postgres psql -U postgres -c "CREATE DATABASE swasthyaqueue;"
docker exec -it swasthyaqueue-postgres psql -U postgres -d swasthyaqueue -c "CREATE SEQUENCE token_number_seq START 1;"
```

**3. Add a `.env` file in `backend/`** (it is git-ignored)

```
JWT_SECRET=put-a-long-random-string-here-at-least-32-characters
```

**4. Run the backend once** so Hibernate creates the tables

```bash
cd backend
./mvnw spring-boot:run        # mvnw.cmd on Windows
```

**5. Add the first hospital and admin.** There is no sign-up page on purpose (an admin creates staff accounts), so the very first admin goes in with SQL. This hash is for the password `test123`, for local use only:

```bash
docker exec -it swasthyaqueue-postgres psql -U postgres -d swasthyaqueue
```

```sql
INSERT INTO hospitals (name, city) VALUES ('Demo Hospital', 'Delhi');
INSERT INTO staff (user_name, password_hash, role, hospital_id)
VALUES ('admin1', '$2a$10$TjPwFViVt9gb82iMpc30v.NHVjXFzQHfu4.kccUZtNOSR.46ZqrQm', 'ADMIN', 1);
```

**6. Run the frontend**

```bash
cd frontend
npm install
ng serve
```

Open http://localhost:4200 and log in as `admin1` / `test123`. From the Admin page you can create departments and staff accounts.

**Timezone gotcha.** On a Windows machine set to India time, Java reports the zone as `Asia/Calcutta`, which the Postgres container rejects. The app sets `Asia/Kolkata` itself on startup, but tests don't go through that code. If tests fail with "Unable to determine Dialect", set `JAVA_TOOL_OPTIONS=-Duser.timezone=Asia/Kolkata`.

## Tests

```bash
cd backend
./mvnw test
```

Most tests are unit tests with fake repositories (Mockito), so they need no database:

- `PatientServiceTest`: a duplicate phone number in the same hospital is rejected.
- `QueueTokenServiceTest`: a token can't be completed twice, a staff member can't complete another hospital's token, wait times use the department average (or the 10 minute default), and an alert fires at exactly 15 minutes.

To run only these (no Docker needed): `./mvnw test -Dtest="*ServiceTest"`. The generated `contextLoads` test starts the whole app, so it needs Postgres and Redis running.

## Things that went wrong, and what I took from them

- **Infinite JSON.** Returning a `Patient` entity made the API loop forever (patient, its tokens, each token's patient, and so on). It happened again with departments and prerequisites. I now return flat response DTOs only.
- **`Role_ADMIN` vs `ROLE_ADMIN`.** One letter case in the JWT filter made every role check fail while the token itself was perfectly valid.
- **The same typo in two places.** A missing colon in a Redis key meant the sync job never found any scores. I fixed one copy, then had to fix the other. Now there's one `buildRedisKey()` method.
- **Angular didn't refresh the screen.** New Angular runs without zone.js, so a plain property updated inside `.subscribe()` doesn't re-render. Signals fixed it.
- **A misspelled setter parameter** (`estimateWaitMinutes`) quietly did nothing. No error, just a null that I traced for a while.
- **Race conditions.** Two staff completing the same token at once, and two requests registering the same phone number at once. I forced both with a 3 second pause to prove them, then fixed them: optimistic locking (`@Version`) for the first, a database unique rule plus a clear error message for the second. Token numbers come from a Postgres sequence for the same reason.
- **Slow work blocking requests.** Alerts run with `@Async`, so a request returns in about 50 ms instead of waiting for the alert. A `alertSent` flag stops the same alert going out on every refresh.

My rough build diary is in [docs/every_step.md](docs/every_step.md). It's messy, written as I went.

## What it doesn't do yet

- The public queue page still returns patient names. For real use it should show token numbers only.
- The JWT is kept in `localStorage`, which is simple but open to XSS. An httpOnly cookie would be safer.
- `ADMIN` can't use the `STAFF` endpoints, because roles are not hierarchical here.
- Hospitals and the first admin are created with SQL. There's no UI for that.
- Tables come from Hibernate's `ddl-auto=update`. A real project would use migrations (Flyway).
- Alerts are only printed to the console, not sent as SMS or email.
- No CI yet. I want GitHub Actions to run the tests on every push.