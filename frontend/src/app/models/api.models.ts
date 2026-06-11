export interface LoginRequest {
  username: string;
  password: string;
}

export interface JwtTokenResponse {
  token: string;
  type?: string;
  expiresIn?: number;
}

export interface Account {
  id: number;
  holderName: string;
  status: string;
  lastupdatedAt: string;
  balance: number;
}

export interface CreateAccountRequest {
  holderName: string;
  password: string;
  status?: string;
  balance: number;
}

export interface TransferRequest {
  fromAccountId: number;
  toAccountId: number;
  amount: number;
}

export interface TransactionLog {
  id: number;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  status: string;
  failureReason?: string;
  idempotencyKey: string;
  createdOn: string;
}

export interface ErrorResponse {
  errorMessage: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export interface SetPasswordRequest {
  newPassword: string;
}

export interface RewardEntry {
  id: number;
  transactionLogId: number;
  pointsAwarded: number;
  description: string;
  createdAt: string;
}

export interface RewardSummary {
  accountId: number;
  totalPoints: number;
  history: RewardEntry[];
}
