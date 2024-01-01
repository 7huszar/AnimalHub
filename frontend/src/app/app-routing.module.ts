import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home.component';
import {LoginComponent} from './pages/login/login.component';
import {MemberProfileComponent} from "./pages/home/member-profile/member-profile.component";
import {AdminPageComponent} from "./pages/admin-page/shelter-admin-page/admin-page.component";
import {RegisterComponent} from "./pages/register/register.component";
import {RegisterConfirmComponent} from "./pages/register/register-confirm/register-confirm.component";
import {AuthGuard} from './guards/auth.guard';
import {Mode} from "./enums/modes";
import {CreateEditComponent} from './pages/animal/create-edit/create-edit.component';
import {ShelterCreateEditComponent} from "./pages/shelter/shelter-create-edit/shelter-create-edit.component";
import {ShelterComponent} from "./pages/shelter/shelter.component";
import {AnimalHomepageComponent} from "./pages/animal-homepage/animal-homepage.component";
import {UserEditComponent} from "./pages/user/create-edit/user-edit.component";
import {PasswordResetComponent} from "./pages/password-reset/password-reset.component";
import {PasswordVerifyComponent} from "./pages/password-reset/password-verify/password-verify.component";
import {ContactFormComponent} from "./pages/contact-form/contact-form.component";


const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/confirm/:token', component: RegisterConfirmComponent},
  {path: 'home', canActivate: mapToCanActivate([AuthGuard]), component: HomeComponent},
  {path: 'home/member-profile', canActivate: mapToCanActivate([AuthGuard]), component: MemberProfileComponent},
  {path: 'admin', canActivate: mapToCanActivate([AuthGuard]), component: AdminPageComponent},
  {path: 'password/request-reset', component: PasswordResetComponent},
  {path: 'password/reset/:token', component: PasswordVerifyComponent},
  {
    path: 'animal', children: [
      {path: 'create/:shelterId', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: Mode.CREATE}},
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: Mode.EDIT}},
      {path: ':id', canActivate: mapToCanActivate([AuthGuard]), component: AnimalHomepageComponent}
    ]
  },
  {
    path: 'shelter', children: [
      {path: 'create', canActivate: mapToCanActivate([AuthGuard]), component: ShelterCreateEditComponent, data: {mode: Mode.CREATE}},
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: ShelterCreateEditComponent, data: {mode: Mode.EDIT}},
      {path: 'contact/:id', canActivate: mapToCanActivate([AuthGuard]), component: ContactFormComponent},
      {path: ':id', canActivate: mapToCanActivate([AuthGuard]), component: ShelterComponent}
    ]
  },
  {
    path: 'users', children: [
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: UserEditComponent}
    ]
  },

  {path: '*', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
