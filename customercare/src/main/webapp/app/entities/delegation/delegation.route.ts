import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDelegation, Delegation } from 'app/shared/model/delegation.model';
import { DelegationService } from './delegation.service';
import { DelegationComponent } from './delegation.component';
import { DelegationDetailComponent } from './delegation-detail.component';
import { DelegationUpdateComponent } from './delegation-update.component';

@Injectable({ providedIn: 'root' })
export class DelegationResolve implements Resolve<IDelegation> {
  constructor(private service: DelegationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDelegation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((delegation: HttpResponse<Delegation>) => {
          if (delegation.body) {
            return of(delegation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Delegation());
  }
}

export const delegationRoute: Routes = [
  {
    path: '',
    component: DelegationComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Delegations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DelegationDetailComponent,
    resolve: {
      delegation: DelegationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Delegations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DelegationUpdateComponent,
    resolve: {
      delegation: DelegationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Delegations'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DelegationUpdateComponent,
    resolve: {
      delegation: DelegationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Delegations'
    },
    canActivate: [UserRouteAccessService]
  }
];
