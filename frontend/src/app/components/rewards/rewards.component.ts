import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { AuthService } from '../../services/auth.service';
import { RewardService } from '../../services/reward.service';
import { RewardSummary, RewardEntry } from '../../models/api.models';

@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatChipsModule
  ],
  templateUrl: './rewards.component.html',
  styleUrls: ['./rewards.component.css']
})
export class RewardsComponent implements OnInit {
  rewardSummary: RewardSummary | null = null;
  isLoading = true;
  errorMessage = '';

  displayedColumns = ['index', 'transactionId', 'points', 'description', 'date'];

  constructor(
    private authService: AuthService,
    private rewardService: RewardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadRewards();
  }

  loadRewards(): void {
    const accountId = this.authService.getAccountId();
    if (!accountId) {
      this.errorMessage = 'No account found. Please log in again.';
      this.isLoading = false;
      return;
    }

    this.rewardService.getRewardSummary(accountId).subscribe({
      next: (summary) => {
        this.rewardSummary = summary;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.errorMessage || 'Failed to load reward data';
        this.isLoading = false;
      }
    });
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleString('en-IN', {
      day: '2-digit', month: 'short', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  }

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
