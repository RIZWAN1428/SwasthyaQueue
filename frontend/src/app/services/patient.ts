import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface PatientResponse{
  id: number;
  fullName: string;
  dateOfBirth: string;
  phoneNumber: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root',
})
export class Patient {
  
  private apiUrl = 'http://localhost:8080/api/patients';

  constructor(private http:HttpClient){}

  registerPatient(fullName: string, dateOfBirth: string, phoneNumber: string){
    //we're not inserting these values into a string — we're building an actual object (which becomes JSON) to send as the request body.
    return this.http.post<PatientResponse>(this.apiUrl, {fullName, dateOfBirth, phoneNumber});
  }
}
