import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {

  const router = inject(Router);
  const token = localStorage.getItem('token');

  if(!token){
    router.navigate(['/login']);
    return false;
  }

  //JSON.parse(atob(token.split('.')[1])) 
  // decodes a JWT's payload in the browser (atob = base64 decode, built into JavaScript).
  const payload = JSON.parse(atob(token.split('.')[1]));
  if(payload.role === 'ADMIN'){
    return true;
  }

  router.navigate(['/departments']);
  return false; 
};
