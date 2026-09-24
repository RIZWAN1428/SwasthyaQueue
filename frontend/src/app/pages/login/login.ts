import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup,FormControl, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth'


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

    //Forms Group represent whole form, containing multiple form control.
    loginForm = new FormGroup({
      userName: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });

    errorMessage = '';

    constructor(private authService: Auth, private router: Router){}

    onSubmit(){
      //! tells TS "trust me, this won't be null."
      const userName = this.loginForm.value.userName!;
      const password = this.loginForm.value.password!;

      //.subscribe({ next, error }):- Angular's httpclient calls return an observable(A stream we subscribe,to
      //get the result)
      this.authService.login(userName, password).subscribe({
        next: (response) => {
          localStorage.setItem('token', response.token);
          this.router.navigate(['/dashboard']);
        },
        error: () => {
          this.errorMessage = 'Invalid userName or password';
        }
      })
    }
}
