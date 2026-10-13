import {HttpInterceptorFn} from '@angular/common/http';
import {AuthService} from './auth.service';
import {inject} from '@angular/core';


export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken()

  if(!token){
    return next(req);
  }

  return next(
    req.clone({
      setHeaders: {Authorization: `Bearer ${token}`}
    })
  );
};
