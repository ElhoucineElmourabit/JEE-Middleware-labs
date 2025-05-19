import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';


export const appHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  
  if(!req.url.includes("/auth/login")){
    let request = req.clone({
      setHeaders: {
        Authorization: 'Bearer ' + authService.accessToken
      }
    });
    return next(request);
  } else {
    return next(req);
  }
  
};
