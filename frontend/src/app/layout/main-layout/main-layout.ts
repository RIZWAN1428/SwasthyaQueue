import { Component } from '@angular/core';
import { Router,RouterLink, RouterOutlet, } from '@angular/router';
interface JwtPayload {
  sub: string;
  role: string;
  iat: number;
  exp: number;
}
@Component({
  selector: 'app-main-layout',
  //"frame" (header + nav links + logout button) that stays the same on every page, with a second, smaller <router-outlet>
  //  inside that frame — that's where the actual page content (Departments, Queue, Admin) gets placed.
  imports: [RouterLink, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})

export class MainLayout {
  
  isLoggedIn = !!localStorage.getItem('token');
  payload: JwtPayload | null  = null;
  constructor(private router: Router){
    const token = localStorage.getItem('token');

    if(token){
      this.payload = JSON.parse(atob(token.split('.')[1]));
    }
  }

  logout(){
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
