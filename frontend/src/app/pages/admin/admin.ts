import { Component, OnInit, signal } from '@angular/core';
import { Department, DepartmentResponse } from '../../services/department';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-admin',
  imports: [ReactiveFormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.scss',
})
export class Admin implements OnInit {

    departments = signal<DepartmentResponse[]>([]);
    createDeptMessage = signal('');
    avgTimeMessage = signal('');
    prerequisiteMessage = signal('');
    registerStaffMessage = signal('');
    
    createDeptForm = new FormGroup({
      name: new FormControl('', Validators.required),
    });

    avgTimeForm = new FormGroup({
      departmentId: new FormControl('', Validators.required),
      avgTime: new FormControl('', Validators.required),
    }); 

    addPrerequisiteForm = new FormGroup({
      departmentId: new FormControl('', Validators.required),
      prerequisiteDepartmentId: new FormControl('', Validators.required),
    });

    registerStaffForm = new FormGroup({
      userName: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
    });

    constructor(private departmentService: Department, private authService: Auth){}

    ngOnInit(){
      this.loadDepartments();
    }
    loadDepartments(){
      this.departmentService.getAllDepartments().subscribe({
        next:(data) => this.departments.set(data),
        error: () => console.log('Failed to load departments'),
      });
    }

    onCreateDepartment(){
      const name = this.createDeptForm.value.name!;

      this.departmentService.createDepartment(name).subscribe({
        next: () => {
          this.createDeptMessage.set('Department created.');
          this.createDeptForm.reset();
          this.loadDepartments();
        },
        error: () => {
          this.createDeptMessage.set('Failed to create department.');
        },
      });
    }

    //Set Average Time for department
    onSetAvgTime(){
      const departmentId = Number(this.avgTimeForm.value.departmentId);
      const avgTime = Number(this.avgTimeForm.value.avgTime);

      this.departmentService.setAvgTime(departmentId, avgTime).subscribe({
        next: () => {
          this.avgTimeMessage.set('Average Time updated');
          this.avgTimeForm.reset();
          this.loadDepartments();
        },
        error: () => {
          this.avgTimeMessage.set('Failed to update.');
        },
      });
    }

    //Link to prerequisites
    onAddPrerequisite(){
      const departmentId = Number(this.addPrerequisiteForm.value.departmentId);
      const prerequisiteDepartmentId = Number(this.addPrerequisiteForm.value.prerequisiteDepartmentId);

      this.departmentService.addPrerequisite(departmentId, prerequisiteDepartmentId).subscribe({
        next: () => {
          this.prerequisiteMessage.set('Department Link with prerequisite.');
          this.addPrerequisiteForm.reset();
          this.loadDepartments();
        },
        error: (err) =>{
          this.prerequisiteMessage.set(err.error?.error || 'Failed to Link');
        }
      })
    }

    //Register Staff
    onRegisterStaff(){
      const userName = this.registerStaffForm.value.userName!;
      const password = this.registerStaffForm.value.password!;

      this.authService.registerStaff(userName, password).subscribe({
        next: () => {
          this.registerStaffMessage.set('Staff registered Successfully');
          this.registerStaffForm.reset();
        },
        error: (err) =>{
          this.registerStaffMessage.set(err.error?.error || 'Failed to Register Staff');
        }
      })
    }
}
