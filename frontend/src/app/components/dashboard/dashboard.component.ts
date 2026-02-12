import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { Account } from '../../models/api.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  account: Account | null = null;
  isLoading = true;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAccountData();
  }

  loadAccountData(): void {
    const accountId = this.authService.getAccountId();
    
    if (!accountId) {
      // If no account ID in storage, try to get it from all accounts
      const username = this.authService.getUsername();
      if (username) {
        this.accountService.getAllAccounts().subscribe({
          next: (accounts) => {
            const userAccount = accounts.find(
              acc => acc.holderName.toLowerCase() === username.toLowerCase()
            );
            
            if (userAccount) {
              this.authService.setAccountId(userAccount.id);
              this.account = userAccount;
            } else {
              this.errorMessage = 'Account not found';
            }
            this.isLoading = false;
          },
          error: (error) => {
            this.errorMessage = error.error?.errorMessage || 'Failed to load account data';
            this.isLoading = false;
          }
        });
      } else {
        this.errorMessage = 'User not authenticated';
        this.isLoading = false;
      }
      return;
    }

    this.accountService.getAccount(accountId).subscribe({
      next: (account) => {
        this.account = account;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.errorMessage || 'Failed to load account data';
        this.isLoading = false;
      }
    });
  }

  navigateToTransfer(): void {
    this.router.navigate(['/transfer']);
  }

  navigateToHistory(): void {
    this.router.navigate(['/history']);
  }

  logout(): void {
    this.authService.logout();
  }

  getStatusColor(): string {
    return this.account?.status === 'ACTIVE' ? 'primary' : 'warn';
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  }
}
