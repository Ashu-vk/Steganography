import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from "../../services/api.service";

@Component({
  selector: 'app-image-card',
  templateUrl: './image-card.component.html',
  styleUrls: ['./image-card.component.css']
})
export class ImageCardComponent implements OnInit {

  @Input()
  public imageSrc: string|undefined;
  @Input()
  public imageId: number|undefined;
  @Input()
  public imageName: string|undefined;

  constructor() { }

  ngOnInit(): void {
  }

}
