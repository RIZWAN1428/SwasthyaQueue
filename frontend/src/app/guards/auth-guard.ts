import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  // /inject() is Angular's way to get a dependency inside a plain function.
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if(token){
    return true;
  }

  router.navigate(['/login']);
  return false;
};
