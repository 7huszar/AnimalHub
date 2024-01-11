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
import {SubscriptionsComponent} from "./pages/subscriptions/subscriptions.component";
import {ContactFormComponent} from "./pages/contact-form/contact-form.component";
import {MemberEditShelterComponent} from "./pages/member.edit.shelter/member.edit.shelter.component";


const routes: Routes = [
  { path: '',
    component: LoginComponent },

  { path: 'register', children: [
      { path: '',
        component: RegisterComponent },

      { path: 'confirm/:token',
        component: RegisterConfirmComponent },
    ] },


  { path: 'home',
    canActivate: mapToCanActivate([AuthGuard]),
    component: HomeComponent,
    data: {access: Mode.USER} },

  { path: 'home/member-profile',
    canActivate: mapToCanActivate([AuthGuard]),
    component: MemberProfileComponent,
    data: {access: Mode.MEMBER} },

  { path: 'admin',
    canActivate: mapToCanActivate([AuthGuard]),
    component: AdminPageComponent,
    data: {access: Mode.ADMIN} },

  { path: 'password', children: [
      { path: 'request-reset',
        component: PasswordResetComponent },

      { path: 'reset/:token',
        component: PasswordVerifyComponent },
    ] },

  { path: 'animal', children: [
      { path: 'create/:shelterId',
        canActivate: mapToCanActivate([AuthGuard]),
        component: CreateEditComponent,
        data: {mode: Mode.CREATE, access: Mode.MEMBER} },

      { path: 'edit/:id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: CreateEditComponent,
        data: {mode: Mode.EDIT, access: Mode.MEMBER} },

      { path: ':id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: AnimalHomepageComponent,
        data: {access: Mode.USER} }
    ] },

  { path: 'shelter', children: [
      { path: 'create',
        canActivate: mapToCanActivate([AuthGuard]),
        component: ShelterCreateEditComponent,
        data: {mode: Mode.CREATE, access: Mode.ADMIN} },

      { path: 'edit/:id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: ShelterCreateEditComponent,
        data: {mode: Mode.EDIT, access: Mode.MEMBER} },

      { path: 'contact/:id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: ContactFormComponent,
        data: {access: Mode.USER} },

      { path: ':id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: ShelterComponent,
        data: {access: Mode.USER}}
    ] },

  { path: 'users', children: [
      { path: 'edit/:id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: UserEditComponent,
        data: {access: Mode.USER} },

      { path: 'subscriptions',
        canActivate: mapToCanActivate([AuthGuard]),
        component: SubscriptionsComponent,
        data: {access: Mode.USER} },

      { path: 'paypal',
        canActivate: mapToCanActivate([AuthGuard]),
        component: SubscriptionsComponent,
        data: {access: Mode.USER} },

      { path: 'shelter/:id',
        canActivate: mapToCanActivate([AuthGuard]),
        component: MemberEditShelterComponent,
        data: {access: Mode.USER}},
    ] },

  {path: '*', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
