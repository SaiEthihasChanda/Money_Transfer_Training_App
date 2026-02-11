import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransferRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  constructor(private http: HttpClient) {}

  transfer(transferRequest: TransferRequest): Observable<string> {
    return this.http.post(
      `${environment.apiUrl}/transfer`,
      transferRequest,
      { responseType: 'text' }
    );
  }
}
