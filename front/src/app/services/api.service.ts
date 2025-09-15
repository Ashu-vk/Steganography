import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, map, Observable, throwError, tap} from "rxjs";
import {QueryParams} from "../utils/CustomTypes";
import {Utils} from "../utils/Utils";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  public baseUrl = "http://localhost:8090";
  constructor(private httpClient: HttpClient) { }

  textGet(url:string): Observable<any> {
    return this.httpClient.get(this.getRestApiUrl() + url, {responseType: "text"})
      .pipe(map((response: any) => (response)), catchError(ApiService.handleError));
  }
  textPost(url:string, body: any,): Observable<any> {
    return this.httpClient.post(this.getRestApiUrl() + url, body,{responseType: "text"})
      .pipe(map((response: any) => (response)), catchError(ApiService.handleError));
  }

  getRestApiUrl() {
    return this.baseUrl + "/rest/api";
  }
  get(url: string, queryParams?: QueryParams): Observable<any> {
    console.log(this.getRestApiUrl() + url)
    return this.httpClient.get(this.getRestApiUrl() + url)
      .pipe(map((response: any) => response), catchError(ApiService.handleError));
  }

  post(url: string, body: any, queryParams?: QueryParams): Observable<any> {
    return this.httpClient.post(this.getRestApiUrl() + url, body)
      .pipe(map((response: any) => response), catchError(ApiService.handleError));
  }

  delete(url: string, queryParams: QueryParams): Observable<any> {
    return this.httpClient.delete(this.getRestApiUrl() + url, {params: Utils.buildHttpParams(queryParams)})
      .pipe(map((response: any) => response), catchError(ApiService.handleError));
  }

  private static handleError(error: HttpErrorResponse): Observable<any> {
    console.log(error);
    return throwError(() => {
       new Error(error.message);
    });
  }
}
