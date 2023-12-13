import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MemberProfileComponent} from "./components/home/member-profile/member-profile.component";
import {AdminPageComponent} from "./components/admin-page/shelter-admin-page/admin-page.component";
import {RegisterComponent} from "./components/register/register.component";
import {RegisterConfirmComponent} from "./components/register/register-confirm/register-confirm.component";
import {AuthGuard} from './guards/auth.guard';
import {Mode} from "./enums/modes";
import {CreateEditComponent} from './components/animal/create-edit/create-edit.component';
import {ShelterCreateEditComponent} from "./components/shelter/shelter-create-edit/shelter-create-edit.component";
import {ShelterComponent} from "./components/shelter/shelter.component";
import {AnimalHomepageComponent} from "./components/animal-homepage/animal-homepage.component";
import {UserAdminPageComponent} from "./components/admin-page/user-admin-page/user-admin-page.component";


const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/confirm/:token', component: RegisterConfirmComponent},
  {path: 'home', canActivate: mapToCanActivate([AuthGuard]), component: HomeComponent},
  {path: 'home/member-profile', canActivate: mapToCanActivate([AuthGuard]), component: MemberProfileComponent},
  {path: 'admin', canActivate: mapToCanActivate([AuthGuard]), component: AdminPageComponent},
  {path: 'users', canActivate: mapToCanActivate([AuthGuard]), component: UserAdminPageComponent},

  {
    path: 'animal', children: [
      {path: 'create', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: Mode.CREATE}},
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: Mode.EDIT}},
      {path: ':id', canActivate: mapToCanActivate([AuthGuard]), component: AnimalHomepageComponent}
    ]
  },
  {
    path: 'shelter', children: [
      {path: '', canActivate: mapToCanActivate([AuthGuard]), component: ShelterComponent},
      {path: 'create', canActivate: mapToCanActivate([AuthGuard]), component: ShelterCreateEditComponent, data: {mode: Mode.CREATE}},
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: ShelterCreateEditComponent, data: {mode: Mode.EDIT}}
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
