import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {UserContact} from "../../models/user-contact";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public userContacts: UserContact[];

  constructor(private userService: UserService) {
  }

  public ngOnInit(): void {
    this.userService.getUserContacts().subscribe((contacts) => {
      this.userContacts = contacts;
    })
  }

}
