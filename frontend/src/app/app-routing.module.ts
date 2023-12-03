import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {AnimalCreateEditMode, CreateEditComponent} from './components/animal/create-edit/create-edit.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},

  // ### ANIMAL ROUTES
  {
    path: 'animal', children: [
      {path: '', canActivate: mapToCanActivate([AuthGuard]), component: HomeComponent},
      {path: 'create', component: CreateEditComponent, data: {mode: AnimalCreateEditMode.create}},
      {path: 'edit/:id', component: CreateEditComponent, data: {mode: AnimalCreateEditMode.edit}}
    ]
  },

  // ### SHELTER ROUTES


  {path: '*', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
