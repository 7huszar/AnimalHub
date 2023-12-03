import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MemberProfileComponent} from "./components/home/member-profile/member-profile.component";
import {AdminPageComponent} from "./components/admin-page/admin-page.component";
import {RegisterComponent} from "./components/register/register.component";
import {RegisterConfirmComponent} from "./components/register/register-confirm/register-confirm.component";
import {AuthGuard} from './guards/auth.guard';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/confirm/:token', component: RegisterConfirmComponent},
  {path: 'home', canActivate: mapToCanActivate([AuthGuard]), component: HomeComponent},
  {path: 'home/member-profile', canActivate: mapToCanActivate([AuthGuard]), component: MemberProfileComponent},
  {path: 'admin', canActivate: mapToCanActivate([AuthGuard]), component: AdminPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
