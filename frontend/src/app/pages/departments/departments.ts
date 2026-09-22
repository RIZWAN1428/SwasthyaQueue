import { Component, OnInit,signal } from '@angular/core';
import { Department, DepartmentResponse } from '../../services/department';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-departments',
  imports: [RouterLink],
  templateUrl: './departments.html',
  styleUrl: './departments.scss',
})
export class Departments implements OnInit {

  //a component property holding the list, starting empty filled once the api call succeeds.
  //creates a reactive value Angular explicitly tracks.(Signal)
  departments = signal<DepartmentResponse[]>([]);

  constructor(private departmentService: Department) { }

  //a special method, angular automatically calls once, right when component is first created and ready.
  ngOnInit() {
    this.departmentService.getAllDepartments().subscribe({
      next: (data) => {
        this.departments.set(data);
      },
      error: (err) => {
        console.log('Failed to Load Departments', err);
      },
    });
  }
}
