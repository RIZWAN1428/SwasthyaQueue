import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface DashboardResponse {
  totalDepartments: number;
  totalPatientsWaiting: number;
  departmentsWithAlerts: string[];
}

@Injectable({
  providedIn: 'root',
})
export class Dashboard {

  private apiUrl = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) {}

  getDashboardSummary() {
    return this.http.get<DashboardResponse>(this.apiUrl);
  }
}