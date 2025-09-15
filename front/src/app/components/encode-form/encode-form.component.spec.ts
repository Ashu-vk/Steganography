import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EncodeFormComponent } from './encode-form.component';

describe('EncodeFormComponent', () => {
  let component: EncodeFormComponent;
  let fixture: ComponentFixture<EncodeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EncodeFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EncodeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
