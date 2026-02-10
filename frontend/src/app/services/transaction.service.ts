import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransactionLog } from '../models/api.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  constructor(private http: HttpClient) {}

  getTransactionById(id: number): Observable<TransactionLog> {
    return this.http.get<TransactionLog>(
      `${environment.apiUrl}/transactions/${id}`
    );
  }

  getAccountTransactionHistory(accountId: number): Observable<TransactionLog[]> {
    return this.http.get<TransactionLog[]>(
      `${environment.apiUrl}/transactions/account/${accountId}`
    );
  }

  getTransactionsByFromAccount(fromAccountId: number): Observable<TransactionLog[]> {
    return this.http.get<TransactionLog[]>(
      `${environment.apiUrl}/transactions/from/${fromAccountId}`
    );
  }

  getTransactionsByToAccount(toAccountId: number): Observable<TransactionLog[]> {
    return this.http.get<TransactionLog[]>(
      `${environment.apiUrl}/transactions/to/${toAccountId}`
    );
  }
}
