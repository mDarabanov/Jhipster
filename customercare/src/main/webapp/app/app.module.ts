import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CustomercareSharedModule } from 'app/shared/shared.module';
import { CustomercareCoreModule } from 'app/core/core.module';
import { CustomercareAppRoutingModule } from './app-routing.module';
import { CustomercareHomeModule } from './home/home.module';
import { CustomercareEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CustomercareSharedModule,
    CustomercareCoreModule,
    CustomercareHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CustomercareEntityModule,
    CustomercareAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class CustomercareAppModule {}
