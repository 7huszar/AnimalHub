import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MemberProfileComponent} from "./components/home/member-profile/member-profile.component";
import {AdminPageComponent} from "./components/admin-page/admin-page.component";
import {RegisterComponent} from "./components/register/register.component";
import {RegisterConfirmComponent} from "./components/register/register-confirm/register-confirm.component";
import {AuthGuard} from './guards/auth.guard';
import {AnimalCreateEditMode, CreateEditComponent} from './components/animal/create-edit/create-edit.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/confirm/:token', component: RegisterConfirmComponent},
  {path: 'home', canActivate: mapToCanActivate([AuthGuard]), component: HomeComponent},
  {path: 'home/member-profile', canActivate: mapToCanActivate([AuthGuard]), component: MemberProfileComponent},
  {path: 'admin', canActivate: mapToCanActivate([AuthGuard]), component: AdminPageComponent},
  {
    path: 'animal', children: [
      {path: 'create', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: AnimalCreateEditMode.create}},
      {path: 'edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: CreateEditComponent, data: {mode: AnimalCreateEditMode.edit}}
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
