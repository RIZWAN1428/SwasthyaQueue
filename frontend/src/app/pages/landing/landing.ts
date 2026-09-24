import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Department, DepartmentResponse } from '../../services/department';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
})
export class Landing implements OnInit {
  isLoggedIn = !!localStorage.getItem('token');
  departments = signal<DepartmentResponse[]>([]);

  // Marquee items highlighting technical engineering and clinical flow from docs/every_step.md
  tickerItems = [
    { icon: '⚡', title: 'Redis In-Memory Priority Cache', tag: 'Sub-1ms Write/Read' },
    { icon: '🔗', title: 'Prerequisite Clinical DAG', tag: 'Blood Test ➔ Consultation' },
    { icon: '⏱️', title: 'Dynamic Aging Multiplier', tag: 'Wait Time × 0.3' },
    { icon: '🚨', title: 'Critical Severity Factor', tag: 'Severity Score × 10' },
    { icon: '🐘', title: 'PostgreSQL ACID Persistence', tag: 'Zero-Collision Sequence' },
    { icon: '🔄', title: 'Two-Tier Sync Daemon', tag: '@Scheduled 30s' },
    { icon: '🛡️', title: 'Stateless JWT Filter', tag: 'Staff & Admin RBAC' },
    { icon: '🔔', title: 'Real-Time Overcrowd Alerts', tag: 'Threshold Alarms' },
    { icon: '📊', title: 'Decoupled Read/Write Layers', tag: 'Zero DB Write Locks' },
    { icon: '🩺', title: 'Automated Clinical Triage', tag: 'Level 1–5 Scoring' }
  ];

  workflowSteps = [
    {
      num: '01',
      title: 'Patient Intake & Validation',
      desc: 'Instant verification of phone uniqueness and demographic capture with JPA repository validation.',
      badge: 'JPA Repository + Phone Check'
    },
    {
      num: '02',
      title: 'Prerequisite Dependency DAG',
      desc: 'System checks if prerequisite clinical departments (e.g. Pathology Blood Test) have COMPLETED status before token creation.',
      badge: 'Enforced Clinical Prerequisites'
    },
    {
      num: '03',
      title: 'Dynamic Redis Priority Scoring',
      desc: 'Calculates real-time priority: (Severity × 10) + (Aging × 0.3) in Redis memory without hitting the database on every queue read.',
      badge: 'Redis In-Memory Key-Value'
    },
    {
      num: '04',
      title: 'Queue Dispatch & 30s Persistence',
      desc: 'Token dispatched to live doctor console. Scheduled worker syncs Redis priority state back to PostgreSQL every 30 seconds.',
      badge: '@Scheduled 30s Batch Syncer'
    }
  ];

  constructor(private departmentService: Department) {}

  ngOnInit() {
    this.departmentService.getAllDepartments().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.departments.set(data);
        } else {
          this.setFallbackDepartments();
        }
      },
      error: () => {
        this.setFallbackDepartments();
      }
    });
  }

  private setFallbackDepartments() {
    this.departments.set([
      { id: 1, name: 'Cardiology OPD', avgTime: 18 },
      { id: 2, name: 'Pathology & Blood Lab', avgTime: 10 },
      { id: 3, name: 'Emergency Triage', avgTime: 5 },
      { id: 4, name: 'Orthopedics Clinic', avgTime: 25 },
      { id: 5, name: 'Radiology & X-Ray', avgTime: 15 },
      { id: 6, name: 'General Medicine', avgTime: 12 },
    ]);
  }
}
