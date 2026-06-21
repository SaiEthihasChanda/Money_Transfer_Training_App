import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormGroupDirective, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { TransferService } from '../../services/transfer.service';
import { Account, TransferRequest } from '../../models/api.models';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule
  ],
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
  @ViewChild(FormGroupDirective) formDirective!: FormGroupDirective;
  transferForm: FormGroup;
  account: Account | null = null;
  isLoading = false;
  isLoadingAccount = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private accountService: AccountService,
    private transferService: TransferService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.transferForm = this.fb.group({
      toAccountId: ['', [Validators.required, Validators.min(1)]],
      amount: ['', [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.loadAccountData();
  }

  loadAccountData(): void {
    const accountId = this.authService.getAccountId();
    
    if (!accountId) {
      this.showError('Account not found. Please login again.');
      this.router.navigate(['/login']);
      return;
    }

    this.accountService.getAccount(accountId).subscribe({
      next: (account) => {
        this.account = account;
        this.isLoadingAccount = false;
      },
      error: (error) => {
        this.showError(error.error?.errorMessage || 'Failed to load account data');
        this.isLoadingAccount = false;
      }
    });
  }

  get toAccountId() {
    return this.transferForm.get('toAccountId');
  }

  get amount() {
    return this.transferForm.get('amount');
  }

  onSubmit(): void {
    if (this.transferForm.invalid) {
      this.showError('Please fill in all required fields correctly.');
      return;
    }

    if (!this.account) {
      this.showError('Account information not available.');
      return;
    }

    const amount = this.transferForm.value.amount;
    
    if (amount > this.account.balance) {
      this.showError('Insufficient balance for this transfer.');
      return;
    }

    const toAccountId = this.transferForm.value.toAccountId;
    
    if (toAccountId === this.account.id) {
      this.showError('Cannot transfer to the same account.');
      return;
    }

    this.isLoading = true;

    const transferRequest: TransferRequest = {
      fromAccountId: this.account.id,
      toAccountId: toAccountId,
      amount: amount
    };

    this.transferService.transfer(transferRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.showSuccess('Transfer completed successfully!');

        // resetForm() also clears the "submitted" state on the form directive,
        // so Material does not re-flag the empty fields as invalid afterwards.
        if (this.formDirective) {
          this.formDirective.resetForm();
        } else {
          this.transferForm.reset();
        }

        // Reload account data to show updated balance
        this.loadAccountData();
      },
      error: (error) => {
        this.isLoading = false;
        this.showError(error.error?.errorMessage || 'Transfer failed. Please try again.');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard']);
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR'
    }).format(amount);
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
