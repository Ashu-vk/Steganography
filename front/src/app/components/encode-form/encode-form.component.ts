import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ApiService} from "../../services/api.service";

@Component({
  selector: 'app-encode-form',
  templateUrl: './encode-form.component.html',
  styleUrls: ['./encode-form.component.css']
})
export class EncodeFormComponent implements OnInit {
  message = "";
   selectedFile?: File;

  constructor(private api: ApiService) { }

  ngOnInit(): void {
  }

  update($event: Event) {
    const input = $event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      console.log("Selected file:", this.selectedFile);
    }
  }

  submit() {
    if (!this.selectedFile || !this.message) {
      console.error("File or message missing");
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);
    formData.append('message', this.message);

    this.api.post('/message', formData).subscribe({
      next: (res) => {
        console.log('Upload success:', res);
      },
      error: (err) => {
        console.error('Upload error:', err);
      }
    });
  }

}
