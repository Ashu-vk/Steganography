import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {EncodeFormComponent} from "./components/encode-form/encode-form.component";
import {GalleryComponent} from "./components/gallery/gallery.component";
import {DecodeGalleryComponent} from "./components/decode-gallery/decode-gallery.component";
import {AuthGuard} from "./guards/auth.guard";

const routes: Routes = [
  {
    path: "home",
    component: HomeComponent,
  },
  {
    path: "encode",
    component: EncodeFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "decode",
    component: DecodeGalleryComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "gallery",
    component: GalleryComponent,
  },
  {
    path: "**",
    redirectTo: "home",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
