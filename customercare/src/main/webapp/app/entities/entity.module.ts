import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.CustomercareLocationModule)
      },
      {
        path: 'delegation',
        loadChildren: () => import('./delegation/delegation.module').then(m => m.CustomercareDelegationModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CustomercareEntityModule {}
