import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Dashboard as DashboardService, DashboardResponse } from '../../services/dashboard';


@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit{

    dashboard = signal<DashboardResponse | null>(null); 

    constructor(private dashboardService: DashboardService) { }

    ngOnInit() {
      this.dashboardService.getDashboardSummary().subscribe({
        next: (data) => {
          this.dashboard.set(data);
        },
        error: (err) => {
          console.log('Failed to Load Dashboard', err);
        },
      });
    }
}
