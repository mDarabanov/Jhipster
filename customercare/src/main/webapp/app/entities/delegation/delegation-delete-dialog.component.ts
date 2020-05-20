import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDelegation } from 'app/shared/model/delegation.model';
import { DelegationService } from './delegation.service';

@Component({
  templateUrl: './delegation-delete-dialog.component.html'
})
export class DelegationDeleteDialogComponent {
  delegation?: IDelegation;

  constructor(
    protected delegationService: DelegationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.delegationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('delegationListModification');
      this.activeModal.close();
    });
  }
}
