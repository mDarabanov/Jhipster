import { Moment } from 'moment';

export interface IDelegation {
  id?: number;
  name?: string;
  description?: string;
  startDate?: Moment;
  endDate?: Moment;
  agendaContentType?: string;
  agenda?: any;
  locationName?: string;
  locationId?: number;
}

export class Delegation implements IDelegation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public agendaContentType?: string,
    public agenda?: any,
    public locationName?: string,
    public locationId?: number
  ) {}
}
