import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TokenStorageService} from '../../services/token-storage.service';
import {UserService} from '../../services/user.service';
import {environment} from '../../../environments/environment';
import {UserInfo} from "../../models/user-info";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public googleURL = environment.apiBaseUrl + 'oauth2/authorization/google?redirect_uri=http://localhost:4200/login'

  constructor(private tokenStorage: TokenStorageService,
              private activatedRoute: ActivatedRoute,
              private userService: UserService,
              private router: Router) {
  }

  public ngOnInit(): void {
    const token = this.activatedRoute.snapshot.queryParamMap.get('token');

    if (this.tokenStorage.getToken()) {
      this.router.navigate(['home']);
    } else if (token) {
      this.tokenStorage.saveToken(token);
      this.userService.getCurrentUser().subscribe(
        user => {
          this.login(user);
        }
      );
    }
  }

  public login(user: UserInfo): void {
    this.tokenStorage.saveUser(user);
    this.router.navigate(['home']);
  }
}
