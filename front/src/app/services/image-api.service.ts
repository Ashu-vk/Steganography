import { Injectable } from '@angular/core';
import {firstValueFrom, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ImageApiService {
  apiUrl = "https://pixabay.com/api/"
  apiKey = "26928134-a25616e872dd978822d89bf5a"
  constructor(private http: HttpClient) { }

  getImages (query: string, perPage: number = 20, page: number = 1) : Observable<any> {
    const url = `${this.apiUrl}?key=${this.apiKey}&q=${encodeURIComponent(query.trim())}&image_type=photo&per_page=${perPage}&page=${page}`;
    return this.http.get<any>(url);
  }
  getImageById(id: number) {
    const url = `${this.apiUrl}?key=${this.apiKey}&id=${id}`;
    return this.http.get<any>(url);
  }

  async getImageByUrl(url: string): Promise<File> {
    const blob: Blob = await firstValueFrom(
      this.http.get(url, { responseType: 'blob' })
    );

    const fileName = url.split('/').pop() || 'image.jpg';
    return new File([blob], fileName, { type: blob.type || 'application/octet-stream' });
  }
}
