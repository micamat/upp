import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { MailComponent } from './registration/mail/mail.component';
import { AccountComponent } from './registration/account/account.component';
import { LoginComponent } from './registration/account/login/login.component';
import { ProfileComponent } from './registration/account/profile/profile.component';
import { TaskComponent } from './registration/account/profile/task/task.component';
import { EditorProfileComponent } from './registration/account/profile/editor-profile/editor-profile.component';
import { ScienceMagazineComponent } from './science-magazine/science-magazine.component';
import { EditorsAndReviewersComponent } from './science-magazine/editors-and-reviewers/editors-and-reviewers.component';

const routes: Routes = [
  {
    path: "registrate",
    component: RegistrationComponent
  },
  {
    path: "mail-sent",
    component: MailComponent
  },
  {
    path: "mail-confirmed",
    component: AccountComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "profile",
    component: ProfileComponent
  },
  {
    path: "task",
    component: TaskComponent
  },
  {
    path: "editor-profile",
    component: EditorProfileComponent
  },
  {
    path: "new-magazine",
    component: ScienceMagazineComponent
  },
  {
    path: "editors-and-reviewers",
    component: EditorsAndReviewersComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
