first  build 3 directory frontend , backend , docs
then from sprin intiializer with 4 dependencies i downloaded the zip, and extract into backend folder.

in backend we run backendapplicatio.java , got the asia/calcutta error, correct it byt writing in main a a system.setproperties to thath time.

Then make a folder entity in that create a file patient.java and write all required code to connect with database by using hibernate. which is internally connectedto department.java, queuetoken.java and tokenstatus.java
nowi build a repository folder with patientrepository.java , for code which actually save data and retreve data for that purpose we write an interface, which basically implement automatically at runtimeall of this(save, findById, findAll, delete) by extending jparepository.noneed to write a boiler plate code anymore.
now created 3 repository file in folder, but why 3 differnt not 1 shared, because later there will be requirment of different queries like findbbyphonenumber, findBydepartment etc. by different repositry so we need that different.
now created service folderin this patientservice....we write service annotation to tell spring this class contains business logic and then also add @Autowired for Dependency Injection.
create a register patientmethod, with checup of same phone number if exit in repository then throw error if not then save that new patient in repositry.
Get a patient by id, using optional(that handle a case of empty patient), use also findbyid(from jpa repossitory) then after handlng error,we got the result.
get all patient , return it as list , by using that same method jpa repository provided.
now we create queuetokenservice, and we inject queuetokenrepo, patient and deparment repo too!!
in this we create, createtoken method..with first check patient exist and dpeartment exist.....isyes then we generate thenew token usingmethod.
now we write generatetoken method, for that we use postgres sequence, nextval()method to get unique value for each request implement in queuetokenreposi and connect in this method.
write a getqueuefordepartment using same methd and also get the real query from queuetokenrep where we write a query to ftech data , by department and status. we keep status waiting, coz we really care abou that now.
Now before writing priority engine method, we decide severity weight and aging weight, which could like gave priority to critical severity so multiply by 10, and for aging we make sure waiting time 30+ minute so multiply 0.3 to adjust priority engine not conflicting with severity and priority.
now will calculateprioroty score, to calculate it....not save it.
now the problem for saving priority queue is every queue view triggers up to N database writes(one per waiting) just to keep priority Score fresh.
for this we use Redis, in memory database to store a value and calculate it live and store in redis. postgres stays the "source of truth" but for frequently changing, recalculated priority score lives in redis. we can give back value to podtgres when need but not on every read.
we will add dependencies in pom.xml for redis. and add host and port in application property.
we write redisconfig class to tell spring how data store in redis, as in redis works with key value paires not java objects.
we convert in redisconfig, turning an object into a storable format, like text or bytes it called serialization and reverse of it deserialization.
in get queue for department, we update the Redis in this method.
now in redis we have a priority score but in postgres it is oudated so we periodic sync it.
for that we use new concept @scheduled, so lets sayfor each 30 second the redis value back to postgres.
we add in backendapplication.java, @eablescheduling. we add a @shechedule  method  in service
now we write controller to check with api the redis and other working with postman.
we create controller for patient. now we create a DTO to make sure how incomingJSOn lokks like.
DTO-what comes from outside and goes from inside, The Entity - what actually llives in the database
wecreate dto folder in that we build patientRegistrationRequest.java which tells the way request comes.
We write postmapping using that dto in controller.
now check till here with postman, all good but error message not good so making globalexception message.
make global exception file, in that add restontrolleradvice to handle excpetionand exception method.
Now a create a department service and department conroller.
now crreate similarlytoken create dtop as thisis complex and also queuetoken controller.
now test all with postman and redis.
now create a get patient in patientcontroller.
now make on file deparment prerequisite in entity, to have this idea of (if patient has consultation, then it should completed blood test),if blood test done and no requirment then consultation go. and also before creating queue token we have to check if patient completed all department requirments.
Now in department entity we map backword , so department check the prequisites.
now add departmentprequisitesrepo....after that final queuetokenservice to check whetehr ptient has completed all the perequisites before lettingthem to create token.
In queuetoken repo we create metho for, does this queutoken exist for this exact patient at exactdpeartment with this exact status?.
For Check in prerequistes before creating token we create  amethod in tokenservice.
nowwe  add a controller for department prerequisites,for that we add a method in department service to call that.
now add complete token method in service to complete token in queue token service.
now add complete token service and method.
Now create a logic for calculate for patient waiting time per deparment.
for this we make add avg time in department and getter and setter forit. and for estimated wait minites in queue token we write get and set.
Now for setavg time we make service and controller and also dto, department response to get structured response.
now build the should alert and added in response and controllerand service.
now we will move to spring security.
Understand a JWT, server generates a one time token when user login with data and digital signature, each time user login it just verified the signature.
write staff entity and staff repository to seach whn u login.
now write secruity Config file.
now build staff service file.
Importing dependency in pom file, it automatically inject security in all api. so we use filters.
we add filter in security config to allow particular login.
now make login method for staff service.
currently in login , we returning object but we will generate a token for that we will inbuild dependency of jwt into pom.xml.
now write a login controller logic to return token.
in security we wrtie a filter to read that token and validate it.
test security with tafff login and department api with admin role.
Add secret key in env file , and connect in application.proerties....so that after every app start we don't have to loginagain and validate token .
resolve the env auto reading errors, now check with postman user can login evenn after restart.

Now fronetend, we choose to first create simple login and register pages. with different routes.
then we run this ng new . --routing --style=scss --skip-tests inside frontend directory, to setup routing from start, and set style scss, and also skip spec.ts file as we are gonna testin junit in backend. this not works so go back in one directory and run this ng new frontend --routing --style=scss --skip-tests.
as for ssr and ssc, said no...mostly it help in seo. so we don't need that. no choose ai one coz here i learning but can use, as it is helpful.
ng generate component pages/login --standalone, here pages will hold like full route views login, register etc.
while components lates use like smallerreusable pieces like button, card, table , row.
now add the this login page to app.route.ts, first import it then add in array with path and component.
now we got token from backend , we have to add with every request so that we need to store but storing in localstorageis a risk of xss(cross site scripting), means through vulnerable input field or third party, or by any JS runnning on that page can access localstorage.
can use httpOnly cookie , which js can't read at all but need more setp, fornow local storage is fine as angular gave xss protection and we can sanitize inputs.
ngmodel :- dierectly controls the data it become messy when forms grow, but reactive forms:- ts file controls the data , html file only displays it. it clean can scale and easy to test.
import reactiveformsmodule in login and create new form in login.ts
now after creating html form, we will create a service file(auth.ts) for to talk to backend api.
then we make httpclient to access appwide so change in app.config.ts
then test with backend add cors in backend. and it works fine.
generate pages/ departments.
build servie of department, then component of departments.
In angular newer version it's defaul to zoneless mode, meaning angular no longer automatically watches every async operation like (.subscribe) and re render, but we have to tell "hey something changed please re render" after async update.for that we use signal.
Now will work on queue, same first sate in route then generate component.
now will create register patient component. after reegister it not able to register because it need staff access for that we need interceptors.
create a auth-interceptor file,  then register in app.config ts file.
now for route page create token page. same add route, add crete token method in service. write create token logic in component
now write a service in queue token and method in queue to fetch all queue token for department for that also add the controller in backend.
create the admin component to manage create department, set avg time, link prequesites. add in routes.
now add createdepartment in department service then in admin.ts add method.
similarly done for avg tiime and alink prerequisite.

now work on style and ui. build main layout and add router outlet, so that it can be in every page except login for that we set up in app routes.
now add guard as auth guard to protect route like which required login.
ng generate guard guards/auth --functional => --functional picks Angular's newer, simpler guard style — a plain function, not a class.
now for admin build guard.
after that updte app routes.
now create a page for register so that staff can register by admin......also link register patient api in navbar.
