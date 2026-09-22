import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

//TS way of describing the shape of data we expect back matching DTP ,  department reposnse
export interface DepartmentResponse{
  id: number;
  name: string;
  //Ts way of saying it could be a number or null.
  avgTime: number|null;
}


//Injectable tells Angular this class can be used with Dependency Injection (DI).
@Injectable({
  providedIn: 'root',
})


export class Department {
    
  private apiUrl = 'http://localhost:8080/api/departments';

  constructor(private http: HttpClient){}

  //Get All departments
  getAllDepartments(){
    return this.http.get<DepartmentResponse[]>(this.apiUrl);
  }
  //Create Department
  createDepartment(name: string){
    return this.http.post<DepartmentResponse>(this.apiUrl, { name });
  }

  //Set Average time for Department
  setAvgTime(departmentId: number, avgTime: number){
    return this.http.put<DepartmentResponse>(`${this.apiUrl}/${departmentId}/avg-time`, avgTime);
  }

  //Link prerequisites
  addPrerequisite(departmentId: number, prerequisiteDepartmentId: number){
    return this.http.post(`${this.apiUrl}/${departmentId}/prerequisites/${prerequisiteDepartmentId}`, {});
  }
}
