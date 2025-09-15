import { Component, OnInit } from '@angular/core';
import {ImageData} from "../../model/image-data";
import {ApiService} from "../../services/api.service";

@Component({
  selector: 'app-decode-gallery',
  templateUrl: './decode-gallery.component.html',
  styleUrls: ['./decode-gallery.component.css']
})
export class DecodeGalleryComponent implements OnInit {



  public images: ImageData[] = [];
  constructor(public apiService: ApiService) { }

  ngOnInit(): void {
    this.apiService.get('/images').subscribe( (data)=> {
        this.images = data;
      }
    )
  }

  reveal(imageId:number) {
    this.apiService.textGet("/reveal/"+ imageId).subscribe( message => {
      // this.toasterService.info(message);
      confirm(message);
    })
  }
}
