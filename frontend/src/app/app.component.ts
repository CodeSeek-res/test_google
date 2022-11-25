import {AfterContentInit, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {TokenStorageService} from './services/token-storage.service';
import {Router} from "@angular/router";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public isLoggedIn$: Observable<boolean>;

  constructor(private tokenStorageService: TokenStorageService,
              private router: Router) {
  }

  public ngOnInit(): void {
    this.isLoggedIn$ = this.tokenStorageService.isLoggedIn();
  }

  public getCurrentUserName(): string {
    return this.tokenStorageService.getUser().displayName;
  }

  public logout(): void {
    this.tokenStorageService.signOut();
    this.router.navigate(['login']);
  }
}
