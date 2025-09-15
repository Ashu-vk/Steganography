import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DecodeGalleryComponent } from './decode-gallery.component';

describe('DecodeGalleryComponent', () => {
  let component: DecodeGalleryComponent;
  let fixture: ComponentFixture<DecodeGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DecodeGalleryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DecodeGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
