import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDelegation } from 'app/shared/model/delegation.model';

type EntityResponseType = HttpResponse<IDelegation>;
type EntityArrayResponseType = HttpResponse<IDelegation[]>;

@Injectable({ providedIn: 'root' })
export class DelegationService {
  public resourceUrl = SERVER_API_URL + 'api/delegations';

  constructor(protected http: HttpClient) {}

  create(delegation: IDelegation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delegation);
    return this.http
      .post<IDelegation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(delegation: IDelegation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(delegation);
    return this.http
      .put<IDelegation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDelegation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDelegation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(delegation: IDelegation): IDelegation {
    const copy: IDelegation = Object.assign({}, delegation, {
      startDate: delegation.startDate && delegation.startDate.isValid() ? delegation.startDate.format(DATE_FORMAT) : undefined,
      endDate: delegation.endDate && delegation.endDate.isValid() ? delegation.endDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((delegation: IDelegation) => {
        delegation.startDate = delegation.startDate ? moment(delegation.startDate) : undefined;
        delegation.endDate = delegation.endDate ? moment(delegation.endDate) : undefined;
      });
    }
    return res;
  }
}
