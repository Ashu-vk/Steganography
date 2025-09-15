import { Component, OnInit } from '@angular/core';
import {ImageApiService} from "../../services/image-api.service";
import {ApiService} from "../../services/api.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-gallery',
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.css']
})
export class GalleryComponent implements OnInit {
  constructor(private imageService: ImageApiService,
              private api: ApiService,
              protected authService: AuthService) { }
  public images: any;
  ngOnInit(): void {
   this.imageService.getImages("").subscribe(data=>{
     this.images = data
     console.log(this.images)
   })
  }

  encrypt(image: any) {
    // console.log(image)
    const msg: string|null = prompt();
    this.imageService.getImageByUrl(image.largeImageURL).then(
      file=> {
        const formData = new FormData();
        formData.append('file', file, image.tags.split(',')[0]+".jpg");
        formData.append('message', msg?msg:"");

        this.api.post('/message', formData).subscribe({
          next: (res) => {
            console.log('Upload success:', res);
          },
          error: (err) => {
            console.error('Upload error:', err);
          }
        });
      })
  }
}
