import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Patient } from '../../services/patient';

@Component({
  selector: 'app-register-patient',
  imports: [ReactiveFormsModule],
  templateUrl: './register-patient.html',
  styleUrl: './register-patient.scss',
})
export class RegisterPatient {

    patientForm = new FormGroup({
      fullName: new FormControl('', Validators.required),
      dateOfBirth: new FormControl('', Validators.required),
      phoneNumber: new FormControl('',Validators.required),
    })

    errorMessage = '';

    constructor(private patientService: Patient, private router: Router){}

    onSubmit(){
      const fullName = this.patientForm.value.fullName!;
      const dateOfBirth = this.patientForm.value.dateOfBirth!;
      const phoneNumber = this.patientForm.value.phoneNumber!;

      this.patientService.registerPatient(fullName, dateOfBirth, phoneNumber).subscribe({
        next: (response) => {
          //Navigates to /create-token/:patientId after success
          this.router.navigate(['/create-token', response.id]);
        },
        error: () =>{
          this.errorMessage = 'Failed to register patient. Phone number may already exist.';
        },
      });
    }
}
