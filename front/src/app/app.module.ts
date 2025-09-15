import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {OAuthModule} from "angular-oauth2-oidc";
import { HttpClientModule} from "@angular/common/http";
import { RootComponent } from './components/root/root.component';
import { TestComponentComponent } from './components/test-component/test-component.component';
import {NgOptimizedImage} from "@angular/common";
import { HomeComponent } from './components/home/home.component';
import { GalleryComponent } from './components/gallery/gallery.component';
import { EncodeFormComponent } from './components/encode-form/encode-form.component';
import {FormsModule} from "@angular/forms";
import { ImageCardComponent } from './components/image-card/image-card.component';
import { DecodeGalleryComponent } from './components/decode-gallery/decode-gallery.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    OAuthModule.forRoot({
      resourceServer: {
        sendAccessToken: true,
        allowedUrls: ["http://localhost:8090/api", "http://localhost:8090/rest/api"]
      }
    }),
    NgOptimizedImage,
    FormsModule,
  ],
  declarations: [
    AppComponent,
    RootComponent,
    TestComponentComponent,
    HomeComponent,
    GalleryComponent,
    EncodeFormComponent,
    ImageCardComponent,
    DecodeGalleryComponent,

  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
