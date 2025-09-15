import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import { HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'front';
  userImage = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e";
  imageError= false;



  constructor(protected authService: AuthService) {

  }

  login() {
    this.authService.login();
  }

  ngOnInit(): void {
  }


  getUserInfo() {
    this.authService.userInfo();
  }

  logout() {
    this.authService.logout();
  }
}
