import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Account, CreateAccountRequest, ChangePasswordRequest, SetPasswordRequest } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  constructor(private http: HttpClient) {}

  createAccount(account: CreateAccountRequest): Observable<string> {
    return this.http.post<string>(`${environment.apiUrl}/createaccount`, account);
  }

  getAccount(id: number): Observable<Account> {
    return this.http.get<Account>(`${environment.apiUrl}/accounts/${id}`);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${environment.apiUrl}/accounts`);
  }

  getBalance(id: number): Observable<number> {
    return this.http.get<Account>(`${environment.apiUrl}/accounts/${id}`)
      .pipe(map(account => account.balance));
  }

  setPassword(accountId: number, request: SetPasswordRequest): Observable<string> {
    return this.http.put<string>(
      `${environment.apiUrl}/accounts/${accountId}/set-password`,
      request
    );
  }

  changePassword(accountId: number, request: ChangePasswordRequest): Observable<string> {
    return this.http.put<string>(
      `${environment.apiUrl}/accounts/${accountId}/change-password`,
      request
    );
  }
}
