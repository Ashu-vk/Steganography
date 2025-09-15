import {QueryParams} from "./CustomTypes";
import {HttpParams} from "@angular/common/http";

 export class Utils {
  static buildHttpParams(params?: QueryParams): HttpParams {
    let httpParams = new HttpParams();
   if(params) {
     Object.entries(params).forEach(([key, value]) => {
       if (key!==null && key!==undefined&& value !== null && value !== undefined) {
         httpParams = httpParams.set(key, String(value));
       }
     });
   }
    return httpParams;
  }
}
