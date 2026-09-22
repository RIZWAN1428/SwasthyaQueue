import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


export interface QueueTokenResponse{
  id: number;
  tokenNumber: string;
  patientId: number;
  patientName: string;
  departmentId: number
  departmentName: string;
  severityScore: number;
  priorityScore: number | null;
  status: string;
  arrivalTimestamp: string;
  estimatedWaitMinutes: number | null;
  shouldAlert: boolean | null;
}
@Injectable({
  providedIn: 'root',
})
export class QueueToken {

  private apiUrl = 'http://localhost:8080/api/queue-tokens';

  constructor(private http:HttpClient){}
  //get queue for department
  getQueueForDepartment(departmentId: number){
    //ex:-http://localhost:8080/api/queue-tokens/department/1
    return this.http.get<QueueTokenResponse[]>(`${this.apiUrl}/department/${departmentId}`);
  }

  //Create token
  createToken(patientId: number, departmentId: number, severityScore: number){
    return this.http.post<QueueTokenResponse>(this.apiUrl,{ patientId, departmentId, severityScore});
  }

  //Complete Token
  completeToken(tokenId: number){
    return this.http.put<QueueTokenResponse>(`${this.apiUrl}/token/${tokenId}`, {});
  }
  //Get All Tokens for Department
  getAllTokensForDepartment(departmentId: number){
    return this.http.get<QueueTokenResponse[]>(`${this.apiUrl}/department/${departmentId}/all`);
  }
  
}
