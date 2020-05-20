import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDelegation, Delegation } from 'app/shared/model/delegation.model';
import { DelegationService } from './delegation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-delegation-update',
  templateUrl: './delegation-update.component.html'
})
export class DelegationUpdateComponent implements OnInit {
  isSaving = false;
  locations: ILocation[] = [];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    agenda: [],
    agendaContentType: [],
    locationId: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected delegationService: DelegationService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delegation }) => {
      this.updateForm(delegation);

      this.locationService.query().subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body || []));
    });
  }

  updateForm(delegation: IDelegation): void {
    this.editForm.patchValue({
      id: delegation.id,
      name: delegation.name,
      description: delegation.description,
      startDate: delegation.startDate,
      endDate: delegation.endDate,
      agenda: delegation.agenda,
      agendaContentType: delegation.agendaContentType,
      locationId: delegation.locationId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('customercareApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const delegation = this.createFromForm();
    if (delegation.id !== undefined) {
      this.subscribeToSaveResponse(this.delegationService.update(delegation));
    } else {
      this.subscribeToSaveResponse(this.delegationService.create(delegation));
    }
  }

  private createFromForm(): IDelegation {
    return {
      ...new Delegation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      agendaContentType: this.editForm.get(['agendaContentType'])!.value,
      agenda: this.editForm.get(['agenda'])!.value,
      locationId: this.editForm.get(['locationId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDelegation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
