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
import { RewardService } from '../../services/reward.service';
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
  rewardPoints = 0;
  isLoading = true;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private rewardService: RewardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAccountData();
  }

  loadAccountData(): void {
    const accountId = this.authService.getAccountId();

    if (!accountId) {
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
              this.loadRewardPoints(userAccount.id);
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
        this.loadRewardPoints(accountId);
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.errorMessage || 'Failed to load account data';
        this.isLoading = false;
      }
    });
  }

  loadRewardPoints(accountId: number): void {
    this.rewardService.getRewardSummary(accountId).subscribe({
      next: (summary) => {
        this.rewardPoints = summary.totalPoints;
      },
      error: () => {
        this.rewardPoints = 0;
      }
    });
  }

  navigateToTransfer(): void {
    this.router.navigate(['/transfer']);
  }

  navigateToHistory(): void {
    this.router.navigate(['/history']);
  }

  navigateToRewards(): void {
    this.router.navigate(['/rewards']);
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
