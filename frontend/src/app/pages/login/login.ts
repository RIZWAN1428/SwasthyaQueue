import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { ThemeService } from '../../services/theme';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  loginForm = new FormGroup({
    userName: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  errorMessage = '';

  constructor(
    private authService: Auth,
    private router: Router,
    public themeService: ThemeService
  ) {}

  onSubmit() {
    const userName = this.loginForm.value.userName!;
    const password = this.loginForm.value.password!;

    this.authService.login(userName, password).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMessage = 'Invalid username or credentials. Please verify your institutional access.';
      },
    });
  }
}
