import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {TokenStorageService} from '../services/token-storage.service';
import {catchError, Observable} from 'rxjs';
import {Router} from "@angular/router";

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private tokenStorageService: TokenStorageService, private router: Router) {
  }

  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.tokenStorageService.getToken();
    if (token != null) {
      request = request.clone({headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)});
    }
    return next.handle(request).pipe(catchError((err) => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 401) {
              this.tokenStorageService.signOut();
              this.router.navigate(['login']);
            }
          }
          throw err;
        }
      )
    )
  }
}
