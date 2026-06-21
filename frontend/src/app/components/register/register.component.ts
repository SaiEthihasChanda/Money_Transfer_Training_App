import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AccountService } from '../../services/account.service';

// Fixed starting balance for every new account
const STARTING_BALANCE = 1000;

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  hidePassword = true;
  hideConfirmPassword = true;
  readonly startingBalance = STARTING_BALANCE;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.registerForm = this.fb.group(
      {
        holderName: ['', [Validators.required, Validators.minLength(3)]],
        password: ['', [Validators.required, passwordStrengthValidator]],
        confirmPassword: ['', [Validators.required]]
      },
      { validators: passwordMatchValidator }
    );
  }

  get holderName() {
    return this.registerForm.get('holderName');
  }

  get password() {
    return this.registerForm.get('password');
  }

  get confirmPassword() {
    return this.registerForm.get('confirmPassword');
  }

  // Individual constraint states, used to render the live checklist
  get passwordChecks() {
    const value: string = this.password?.value || '';
    return {
      length: value.length >= 8,
      uppercase: /[A-Z]/.test(value),
      lowercase: /[a-z]/.test(value),
      number: /[0-9]/.test(value),
      special: /[^A-Za-z0-9]/.test(value)
    };
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.errorMessage = 'Please fix the highlighted fields before continuing.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload = {
      holderName: this.registerForm.value.holderName,
      password: this.registerForm.value.password,
      balance: STARTING_BALANCE,
      status: 'ACTIVE'
    };

    this.accountService.createAccount(payload).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open(
          'Account created successfully! Please sign in.',
          'Close',
          {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
            panelClass: ['success-snackbar']
          }
        );
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage =
          error.error?.errorMessage ||
          error.error ||
          'Could not create account. Please try again.';
      }
    });
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.hideConfirmPassword = !this.hideConfirmPassword;
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}

/**
 * Validates a password against the security constraints:
 * min 8 chars, one uppercase, one lowercase, one number, one special char.
 */
export function passwordStrengthValidator(
  control: AbstractControl
): ValidationErrors | null {
  const value: string = control.value || '';
  if (!value) {
    return null; // 'required' handles the empty case
  }

  const errors: ValidationErrors = {};
  if (value.length < 8) errors['minLength'] = true;
  if (!/[A-Z]/.test(value)) errors['uppercase'] = true;
  if (!/[a-z]/.test(value)) errors['lowercase'] = true;
  if (!/[0-9]/.test(value)) errors['number'] = true;
  if (!/[^A-Za-z0-9]/.test(value)) errors['special'] = true;

  return Object.keys(errors).length ? errors : null;
}

/**
 * Cross-field validator ensuring the confirm password matches.
 * Sets/clears only the 'mismatch' error so other errors (e.g. required)
 * on the confirm field are preserved.
 */
export function passwordMatchValidator(
  group: AbstractControl
): ValidationErrors | null {
  const passwordCtrl = group.get('password');
  const confirmCtrl = group.get('confirmPassword');
  if (!passwordCtrl || !confirmCtrl) {
    return null;
  }

  // Don't clobber other errors already present on the confirm field.
  if (confirmCtrl.errors && !confirmCtrl.errors['mismatch']) {
    return null;
  }

  if (confirmCtrl.value && passwordCtrl.value !== confirmCtrl.value) {
    confirmCtrl.setErrors({ mismatch: true });
    return { mismatch: true };
  }

  // Passwords match (or confirm is empty) — clear any prior mismatch flag.
  confirmCtrl.setErrors(null);
  return null;
}
