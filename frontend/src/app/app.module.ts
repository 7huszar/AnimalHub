import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {NgxPaginationModule} from 'ngx-pagination';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {CreateEditComponent} from './pages/animal/create-edit/create-edit.component';
import {LangSelectComponent} from './components/lang-select/lang-select.component';
import {MemberProfileComponent} from './pages/home/member-profile/member-profile.component';
import {AdminPageComponent} from './pages/admin-page/shelter-admin-page/admin-page.component';
import {RegisterComponent} from './pages/register/register.component';
import {RegisterConfirmComponent} from './pages/register/register-confirm/register-confirm.component';
import {AnimalCardComponent} from './pages/home/animal-card/animal-card.component';
import {ShelterComponent} from "./pages/shelter/shelter.component";

import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import {ShelterCreateEditComponent} from './pages/shelter/shelter-create-edit/shelter-create-edit.component';
import {AnimalListComponent} from './components/animal-list/animal-list.component';
import {NgOptimizedImage} from "@angular/common";
import {ConfirmDeleteDialogComponent} from "./components/confirm-delete-dialog/confirm-delete-dialog.component";
import {UserEditComponent} from "./pages/user/create-edit/user-edit.component";
import {ContentComponent} from "./components/content/content.component";
import {AnimalHomepageComponent} from "./pages/animal-homepage/animal-homepage.component";
import { PasswordResetComponent } from './pages/password-reset/password-reset.component';
import { PasswordVerifyComponent } from './pages/password-reset/password-verify/password-verify.component';
import { CircleLoadComponent } from './components/circle-load/circle-load.component';
import { SubscriptionsComponent } from './pages/subscriptions/subscriptions.component';
import { FavoritesCardComponent } from './components/home/favorites-card/favorites-card.component';
import { ContactFormComponent } from './pages/contact-form/contact-form.component';

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
    AnimalHomepageComponent,
    AdminPageComponent,
    AnimalCardComponent,
    RegisterComponent,
    RegisterConfirmComponent,
    ShelterCreateEditComponent,
    ShelterComponent,
    AnimalListComponent,
    ConfirmDeleteDialogComponent,
    UserEditComponent,
    ContentComponent,
    FavoritesCardComponent,
    PasswordResetComponent,
    PasswordVerifyComponent,
    CircleLoadComponent,
    ContactFormComponent,
    SubscriptionsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    NgxPaginationModule,
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
