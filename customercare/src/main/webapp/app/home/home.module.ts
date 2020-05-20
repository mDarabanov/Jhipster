import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CustomercareSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [CustomercareSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class CustomercareHomeModule {}
