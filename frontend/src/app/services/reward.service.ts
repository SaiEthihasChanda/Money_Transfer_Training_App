import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RewardSummary } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RewardService {
  constructor(private http: HttpClient) {}

  getRewardSummary(accountId: number): Observable<RewardSummary> {
    return this.http.get<RewardSummary>(
      `${environment.apiUrl}/rewards/${accountId}/summary`
    );
  }
}
