import { Injectable } from '@angular/core';
import {OAuthService} from "angular-oauth2-oidc";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {authConfig} from "../constants/AuthConfig";
import {BehaviorSubject, Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn$ = new BehaviorSubject(false);
  public isLoggedIn$: Observable<boolean> = this.loggedIn$.asObservable();
  constructor(private oAuthService: OAuthService,
              private router: Router,
              private http: HttpClient) {
    oAuthService.configure(authConfig)
    this.oAuthService.loadDiscoveryDocumentAndTryLogin().then( ()=>{
      this.router.navigate(['/']).then();
      if (this.oAuthService.hasValidAccessToken()) {
        this.loggedIn$.next(true)
      }}
    );
  }

  get isLoggedIn(): boolean {
    if(!this.oAuthService.hasValidAccessToken()){
      this.loggedIn$.next(false);
    }
    return this.loggedIn$.value;
  }

  login() {
      if (this.oAuthService.hasValidAccessToken()) {
        console.log("token",this.oAuthService.getAccessToken())
      } else {
        this.oAuthService.initLoginFlow('state', {
          prompt: 'login',
          ui_locales: 'en',
          login_hint: 'john@example.com',
        });
      }
    };
  logout() {
    this.oAuthService.logoutUrl = "http://localhost:8090/logout"
    this.oAuthService.revokeTokenAndLogout({
      client_id: this.oAuthService.clientId,
      postLogoutRedirectUri: 'http://localhost:4200',
    }).then(r => {
      this.loggedIn$.next(false);
      window.location.href = "http://localhost:4200"
    });
    // this.revoke();
  }

  revoke() {

    let dt = new HttpHeaders();

    // dt.set("token", this.oAuthService.getAccessToken());
    // dt.set("token_type_hint", "access_token");
    dt.set("Authorization", "Basic " + window.btoa(authConfig.clientId +":"+authConfig.dummyClientSecret));
    dt.set("client_secret", "password");
    let body = new FormData();
    body.set("token", this.oAuthService.getAccessToken());
    body.set("token_type_hint", "access_token");
    this.http.post("http://localhost:8090/oauth2/revoke", body, {headers:dt}).subscribe(av=>{
      console.log(av)
    })

  }

  getToken() {
    console.log(this.oAuthService.hasValidAccessToken())
    console.log(this.oAuthService.getAccessToken())
    console.log(this.oAuthService.getIdToken())
    console.log(this.oAuthService.getRefreshToken())

  }


  public  userInfo() {
    this.oAuthService.loadUserProfile().then(user=> console.log(user))
  }
}
