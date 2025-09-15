import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {OAuthService} from "angular-oauth2-oidc";
import {ApiService} from "../../services/api.service";
import  {tap} from "rxjs";

@Component({
  selector: 'app-test-component',
  templateUrl: './test-component.component.html',
  styleUrls: ['./test-component.component.css']
})
export class TestComponentComponent implements OnInit {

  constructor(private api: ApiService,
              private http: HttpClient,
              private oauth: OAuthService) { }
  ngOnInit(): void {
  }
  getCurrentUser() {
    console.log("" );
  }
  getPrivateMessage() {
    // console.log(this.api === undefined)
    // this.api.get("/api/private/message").subscribe(m => {
    //   console.log(m)
    // })
    console.log(this.oauth.hasValidAccessToken())
    console.log(this.oauth.getAccessToken())
   this.http.get("http://localhost:8090/api/private/message", {
      responseType: 'text'
    }).subscribe( m => {
     console.log(m)
   });
  }
  getPrivateMessage2() {
    this.api.textGet("/private/message").subscribe(m => {
      console.log(m)
    })
  }
  getPublicMessage() {
  }
}
