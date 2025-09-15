import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'http://localhost:8090',
  redirectUri: 'http://localhost:4200/',
  postLogoutRedirectUri: 'http://localhost:4200/home',
  clientId: 'oidc-client',
  dummyClientSecret: 'password',
  responseType: 'code',
  showDebugInformation: true,
  scope: 'openid',
  disablePKCE: false,
  logoutUrl : "http://localhost:8090/logout",
  sessionChecksEnabled:false,
 clearHashAfterLogin: false,
  sessionCheckIntervall: 3000,
  skipIssuerCheck: false,

};
