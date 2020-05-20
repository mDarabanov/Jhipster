import { IDelegation } from 'app/shared/model/delegation.model';
import { LocationType } from 'app/shared/model/enumerations/location-type.model';

export interface ILocation {
  id?: number;
  name?: string;
  type?: LocationType;
  longitude?: number;
  latitude?: number;
  rating?: number;
  delegations?: IDelegation[];
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public name?: string,
    public type?: LocationType,
    public longitude?: number,
    public latitude?: number,
    public rating?: number,
    public delegations?: IDelegation[]
  ) {}
}
