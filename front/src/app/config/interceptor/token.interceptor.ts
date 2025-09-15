import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpHeaders
} from '@angular/common/http';
import {Observable, catchError, throwError} from 'rxjs';
import {OAuthService} from "angular-oauth2-oidc";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private oAuthService: OAuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token:string = this.oAuthService.getAccessToken();
    if(token.length>0) {
      const clonedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next.handle(clonedRequest).pipe(catchError((error) => {
        return throwError(error);
      })) as any;
    }
    return next.handle(request);
  }
}
