import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Departments } from './pages/departments/departments';
import { Queue } from './pages/queue/queue';
import { RegisterPatient } from './pages/register-patient/register-patient';
import { CreateToken } from './pages/create-token/create-token';
import { Admin } from './pages/admin/admin';
import { MainLayout } from './layout/main-layout/main-layout';
import { authGuard } from './guards/auth-guard';
import { adminGuard } from './guards/admin-guard';
import { Register } from './pages/register/register';
import { Dashboard } from './pages/dashboard/dashboard';

export const routes: Routes = [
    {path: 'login', component: Login},
    { path: 'register', component: Register },
    {
        path: '',
        component: MainLayout,
        children:[
            {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
            {path: 'dashboard', component: Dashboard, canActivate: [authGuard]},
            {path: 'departments', component: Departments, canActivate: [authGuard]},
            {path: 'queue/:id', component: Queue, canActivate: [authGuard]},
            {path: 'register-patient', component: RegisterPatient, canActivate: [authGuard]},
            {path: 'create-token/:patientId', component: CreateToken, canActivate: [authGuard]},
            {path: 'admin', component: Admin, canActivate: [adminGuard]},
        ],
    },
];
