import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Departments } from './pages/departments/departments';
import { Queue } from './pages/queue/queue';
import { RegisterPatient } from './pages/register-patient/register-patient';
import { CreateToken } from './pages/create-token/create-token';
import { Admin } from './pages/admin/admin';
import { MainLayout } from './layout/main-layout/main-layout';

export const routes: Routes = [
    {path: 'login', component: Login},
    {
        path: '',
        component: MainLayout,
        children:[
            {path: 'departments', component: Departments},
            {path: 'queue/:id', component: Queue},
            {path: 'register-patient', component: RegisterPatient}, 
            {path: 'create-token/:patientId', component: CreateToken},
            {path: 'admin', component:Admin},
        ],
    },
];
