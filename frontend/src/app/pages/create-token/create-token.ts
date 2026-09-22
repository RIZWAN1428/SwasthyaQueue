import { Component, OnInit, signal } from '@angular/core';
import { Department, DepartmentResponse } from '../../services/department';
import { FormControl, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { QueueToken } from '../../services/queue-token';

@Component({
  selector: 'app-create-token',
  imports: [ReactiveFormsModule],
  templateUrl: './create-token.html',
  styleUrl: './create-token.scss',
})
export class CreateToken implements OnInit {

  departments = signal<DepartmentResponse[]>([]);
  //! tells TypeScript "trust me, this will definitely be set before it's used
  patiendId!: number;
  errorMessage = '';

  tokenForm = new FormGroup({
    departmentId: new FormControl('', Validators.required),
    severityScore: new FormControl('', Validators.required),
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private queueTokenService: QueueToken,
    private departmentService: Department
  ) {}

  ngOnInit() {
    this.patiendId = Number(this.route.snapshot.paramMap.get('patientId'));

    this.departmentService.getAllDepartments().subscribe({
      next: (data) => this.departments.set(data),

      error: () => console.log('Failed to load deprtments'),
    });
  }

  OnSubmit(){
    const departmentId = Number(this.tokenForm.value.departmentId);
    const severityScore = Number(this.tokenForm.value.severityScore);

    this.queueTokenService.createToken(this.patiendId, departmentId,  severityScore).subscribe({
      next: (response) =>  {
        this.router.navigate(['/queue', departmentId]);
      },
      error: (err) =>{
        //err.error?.error-backend's error responses look like {"error": "message"}; this pulls that exact message out  falling back to a generic message if it's missing.
        this.errorMessage = err.error?.error || 'Failed to create token.';
      },
    })
  }
}
