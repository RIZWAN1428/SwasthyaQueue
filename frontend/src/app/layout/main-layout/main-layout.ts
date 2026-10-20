import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

interface JwtPayload {
  sub: string;
  role: string;
  iat: number;
  exp: number;
}

@Component({
  selector: 'app-main-layout',
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayout {
  isLoggedIn = !!localStorage.getItem('token');
  payload: JwtPayload | null = null;

  constructor(private router: Router) {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        this.payload = JSON.parse(atob(token.split('.')[1]));
      } catch {
        this.payload = null;
      }
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
