import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {UserContact} from "../models/user-contact";

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {
  }

  public getUserContacts(): Observable<UserContact[]> {
    return this.http.get<UserContact[]>(environment.apiBaseUrl + 'api/user/me/contacts');
  }

  public getCurrentUser(): Observable<any> {
    return this.http.get(environment.apiBaseUrl + 'api/user/me');
  }
}
