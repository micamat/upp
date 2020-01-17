import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { UnauthorizedGuard } from './guard/unauthorized.guard';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MailComponent } from './registration/mail/mail.component';
import { AccountComponent } from './registration/account/account.component';
import { LoginComponent } from './registration/account/login/login.component';
import { ProfileComponent } from './registration/account/profile/profile.component';

import { MatListModule } from '@angular/material/list';
import { TaskComponent } from './registration/account/profile/task/task.component';
import { ScienceMagazineComponent } from './science-magazine/science-magazine.component';
import { EditorProfileComponent } from './registration/account/profile/editor-profile/editor-profile.component';
import { EditorsAndReviewersComponent } from './science-magazine/editors-and-reviewers/editors-and-reviewers.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    MailComponent,
    AccountComponent,
    LoginComponent,
    ProfileComponent,
    TaskComponent,
    ScienceMagazineComponent,
    EditorProfileComponent,
    EditorsAndReviewersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatListModule
  ],
  providers: [
    UnauthorizedGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
