import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { TransactionLog } from '../../models/api.models';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    RouterLink
  ],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  transactions: TransactionLog[] = [];
  isLoading = true;
  errorMessage = '';
  accountId: number | null = null;
  
  displayedColumns: string[] = ['id', 'date', 'type', 'account', 'amount', 'status'];

  constructor(
    private authService: AuthService,
    private transactionService: TransactionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadTransactionHistory();
  }

  loadTransactionHistory(): void {
    this.accountId = this.authService.getAccountId();
    
    if (!this.accountId) {
      this.errorMessage = 'Account not found. Please login again.';
      this.isLoading = false;
      return;
    }

    this.transactionService.getAccountTransactionHistory(this.accountId).subscribe({
      next: (transactions) => {
        this.transactions = transactions.sort((a, b) => 
          new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime()
        );
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.errorMessage || 'Failed to load transaction history';
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  getTransactionType(transaction: TransactionLog): string {
    return transaction.fromAccountId === this.accountId ? 'DEBIT' : 'CREDIT';
  }

  getOtherAccountId(transaction: TransactionLog): number {
    return transaction.fromAccountId === this.accountId 
      ? transaction.toAccountId 
      : transaction.fromAccountId;
  }

  getTransactionTypeClass(transaction: TransactionLog): string {
    return this.getTransactionType(transaction) === 'DEBIT' 
      ? 'transaction-debit' 
      : 'transaction-credit';
  }

  getTransactionTypeIcon(transaction: TransactionLog): string {
    return this.getTransactionType(transaction) === 'DEBIT' 
      ? 'arrow_upward' 
      : 'arrow_downward';
  }

  getStatusClass(status: string): string {
    return status === 'SUCCESS' ? 'status-success' : 'status-failed';
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
