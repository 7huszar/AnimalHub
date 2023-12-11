import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { CreateEditComponent } from './components/animal/create-edit/create-edit.component';
import { LangSelectComponent } from './components/lang-select/lang-select.component';
import { MemberProfileComponent } from './components/home/member-profile/member-profile.component';
import { AdminPageComponent } from './components/admin-page/admin-page.component';
import { RegisterComponent } from './components/register/register.component';
import { RegisterConfirmComponent } from './components/register/register-confirm/register-confirm.component';
import { AnimalCardComponent } from './components/home/animal-card/animal-card.component';
import {ShelterComponent} from "./components/shelter/shelter.component";

import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import { ShelterCreateEditComponent } from './components/shelter/shelter-create-edit/shelter-create-edit.component';
import { AnimalListComponent } from './components/animal-list/animal-list.component';
import {NgOptimizedImage} from "@angular/common";

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    CreateEditComponent,
    LangSelectComponent,
    MemberProfileComponent,
    AdminPageComponent,
    AnimalCardComponent,
    RegisterComponent,
    RegisterConfirmComponent,
    ShelterCreateEditComponent,
    ShelterComponent,
    AnimalListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      },
      defaultLanguage: 'de'
    }),
    NgOptimizedImage
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
