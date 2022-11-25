import {Injectable} from '@angular/core';
import {UserInfo} from "../models/user-info";
import {BehaviorSubject, Observable} from "rxjs";

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  public loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(!!this.getUser());

  public signOut(): void {
    sessionStorage.clear();
    this.loggedIn.next(false);
  }

  public saveToken(token: string): void {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return <string>sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: any): void {
    sessionStorage.removeItem(USER_KEY);
    sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.loggedIn.next(true);
  }

  public getUser(): UserInfo {
    return JSON.parse(<string>sessionStorage.getItem(USER_KEY));
  }

  public isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }
}
