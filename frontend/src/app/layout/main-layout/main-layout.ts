import { Component } from '@angular/core';
import { Router,RouterLink, RouterOutlet, } from '@angular/router';

@Component({
  selector: 'app-main-layout',
  //"frame" (header + nav links + logout button) that stays the same on every page, with a second, smaller <router-outlet>
  //  inside that frame — that's where the actual page content (Departments, Queue, Admin) gets placed.
  imports: [RouterLink, RouterOutlet],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayout {
  constructor(private router: Router){}

  logout(){
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
