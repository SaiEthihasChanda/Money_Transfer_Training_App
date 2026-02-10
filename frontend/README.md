# Money Transfer System - Angular Frontend

A modern, responsive Angular application for managing money transfers and account transactions. Built with Angular 17, Angular Material, and TypeScript.

## 🎯 Features Implemented

### ✅ Authentication & Security
- **Login System** - Secure authentication with JWT tokens
- **Auth Guard** - Route protection for authenticated users
- **HTTP Interceptor** - Automatic JWT token injection in API requests
- **Token Management** - Secure token storage and validation
- **Session Management** - Auto-logout on token expiration

### ✅ Core Components

#### 1. Login Component (`/login`)
- User authentication with username/password
- Form validation with error messages
- Password visibility toggle
- Responsive error handling
- Auto-redirect after successful login

#### 2. Dashboard Component (`/dashboard`)
- Welcome banner with user name
- Real-time balance display
- Account status indicator
- Quick action cards for Transfer & History
- Last updated timestamp
- Material Design cards with gradient styling

#### 3. Transfer Component (`/transfer`)
- Current balance display
- Transfer form with validation
- Amount input with balance check
- Destination account ID validation
- Real-time remaining balance calculation
- Success/Error notifications (SnackBar)
- Transfer confirmation
- Auto-refresh balance after transfer

#### 4. History Component (`/history`)
- Complete transaction history table
- Transaction type badges (DEBIT/CREDIT)
- Status indicators (SUCCESS/FAILED)
- Date/time formatting
- Color-coded amounts (red for debit, green for credit)
- Transaction count summary
- Responsive table design
- Empty state handling

### ✅ Services

#### AuthService
```typescript
- login(credentials): Observable<JwtTokenResponse>
- logout(): void
- isAuthenticated(): boolean
- getToken(): string | null
- getUsername(): string | null
- getAccountId(): number | null
```

#### AccountService
```typescript
- getAccount(id): Observable<Account>
- getAllAccounts(): Observable<Account[]>
- createAccount(account): Observable<string>
- setPassword(accountId, request): Observable<string>
- changePassword(accountId, request): Observable<string>
```

#### TransferService
```typescript
- transfer(transferRequest): Observable<string>
```

#### TransactionService
```typescript
- getAccountTransactionHistory(accountId): Observable<TransactionLog[]>
- getTransactionById(id): Observable<TransactionLog>
- getTransactionsByFromAccount(fromAccountId): Observable<TransactionLog[]>
- getTransactionsByToAccount(toAccountId): Observable<TransactionLog[]>
```

## 🚀 Setup Instructions

### Prerequisites
- Node.js (v18 or higher)
- npm (v9 or higher)
- Angular CLI (v17 or higher)

### Installation Steps

1. **Navigate to the frontend directory**
```bash
cd Money_Transfer_Training_App/frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Install Angular CLI (if not installed globally)**
```bash
npm install -g @angular/cli@17
```

4. **Verify backend is running**
   - Ensure the Spring Boot backend is running on `http://localhost:8080`
   - Database should be configured and accessible
   - Test backend health: `http://localhost:8080/api/v1/accounts`

5. **Start the development server**
```bash
npm start
# or
ng serve
```

6. **Access the application**
   - Open browser and navigate to: `http://localhost:4200`
   - Login page will be displayed

## 🔧 Configuration

### API Endpoint Configuration
Update the API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1'  // Change if backend runs on different port
};
```

### Build for Production
```bash
ng build --configuration production
```
Output will be in `dist/money-transfer-frontend/`

## 📱 Application Flow

### 1. User Authentication
```
Login Page (/login)
  ↓
Enter username & password
  ↓
Submit credentials to backend
  ↓
Receive JWT token
  ↓
Store token in localStorage
  ↓
Redirect to Dashboard
```

### 2. Dashboard Navigation
```
Dashboard (/dashboard)
  ↓
View account balance & info
  ↓
Choose action:
  - Transfer Money → /transfer
  - View History → /history
  - Logout → /login
```

### 3. Money Transfer
```
Transfer Page (/transfer)
  ↓
View current balance
  ↓
Enter destination account ID & amount
  ↓
Validate: sufficient balance, valid account
  ↓
Submit transfer request
  ↓
Display success/error message
  ↓
Refresh balance
```

### 4. Transaction History
```
History Page (/history)
  ↓
Fetch all transactions
  ↓
Display in table:
  - Transaction ID
  - Date & Time
  - Type (DEBIT/CREDIT)
  - Account (From/To)
  - Amount
  - Status
```

## 🎨 Design & UI

### Material Design Components Used
- **MatCard** - Card containers for content
- **MatToolbar** - Navigation headers
- **MatFormField** - Input fields
- **MatButton** - Action buttons
- **MatIcon** - Material icons
- **MatTable** - Transaction history table
- **MatChip** - Status badges
- **MatSnackBar** - Notifications
- **MatSpinner** - Loading indicators
- **MatDialog** - Modal dialogs

### Color Scheme
- **Primary**: Indigo (#667eea)
- **Secondary**: Purple (#764ba2)
- **Success**: Green (#4caf50)
- **Error**: Red (#f44336)
- **Gradient Background**: Linear gradient (135deg, #667eea to #764ba2)

### Responsive Design
- Mobile-first approach
- Breakpoints at 768px for tablets and phones
- Flexible grid layouts
- Touch-friendly buttons and inputs

## 📂 Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── login/
│   │   │   │   ├── login.component.ts
│   │   │   │   ├── login.component.html
│   │   │   │   └── login.component.css
│   │   │   ├── dashboard/
│   │   │   │   ├── dashboard.component.ts
│   │   │   │   ├── dashboard.component.html
│   │   │   │   └── dashboard.component.css
│   │   │   ├── transfer/
│   │   │   │   ├── transfer.component.ts
│   │   │   │   ├── transfer.component.html
│   │   │   │   └── transfer.component.css
│   │   │   └── history/
│   │   │       ├── history.component.ts
│   │   │       ├── history.component.html
│   │   │       └── history.component.css
│   │   ├── services/
│   │   │   ├── auth.service.ts
│   │   │   ├── account.service.ts
│   │   │   ├── transfer.service.ts
│   │   │   └── transaction.service.ts
│   │   ├── interceptors/
│   │   │   └── auth.interceptor.ts
│   │   ├── guards/
│   │   │   └── auth.guard.ts
│   │   ├── models/
│   │   │   └── api.models.ts
│   │   ├── app.component.ts
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── environments/
│   │   ├── environment.ts
│   │   └── environment.prod.ts
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json
├── package.json
├── tsconfig.json
└── tsconfig.app.json
```

## 🔐 Security Features

1. **JWT Authentication** - Secure token-based authentication
2. **Route Guards** - Protected routes requiring authentication
3. **HTTP Interceptor** - Automatic token attachment to requests
4. **Token Expiration** - Auto-logout on token expiry
5. **CORS Handling** - Backend CORS configuration support
6. **Input Validation** - Form validation to prevent invalid data
7. **Error Handling** - Graceful error handling with user feedback

## 🧪 Testing Credentials

Use these test accounts (assuming they exist in your backend):

```
Username: Alice
Password: [set via backend]

Username: Bob
Password: [set via backend]
```

## 📝 API Integration

### Backend Endpoints Used

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/login` | User authentication |
| POST | `/api/v1/auth/validate` | Token validation |
| GET | `/api/v1/accounts` | Get all accounts |
| GET | `/api/v1/accounts/{id}` | Get account by ID |
| POST | `/api/v1/createaccount` | Create new account |
| POST | `/api/v1/transfer` | Transfer money |
| GET | `/api/v1/transactions/account/{accountId}` | Get account transactions |
| PUT | `/api/v1/accounts/{id}/set-password` | Set password |
| PUT | `/api/v1/accounts/{id}/change-password` | Change password |

## 🐛 Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure backend has CORS configured for `http://localhost:4200`
   - Check Spring Security configuration

2. **401 Unauthorized**
   - Token might be expired - logout and login again
   - Check if backend JWT secret matches

3. **404 Not Found**
   - Verify backend is running on port 8080
   - Check `environment.ts` API URL configuration

4. **Build Errors**
   - Clear node_modules: `rm -rf node_modules && npm install`
   - Clear Angular cache: `ng cache clean`

5. **Port Already in Use**
   - Change port: `ng serve --port 4201`
   - Or kill process using port 4200

## 📊 Performance Optimizations

- **Standalone Components** - Faster loading, smaller bundles
- **Lazy Loading** - Route-based code splitting (ready for implementation)
- **OnPush Change Detection** - Optimized rendering (can be added)
- **HTTP Caching** - Request optimization (can be enhanced)
- **Production Build** - AOT compilation, tree-shaking, minification

## 🚀 Future Enhancements

### Planned Features
- [ ] Transaction filtering by date range
- [ ] Export transactions to CSV/PDF
- [ ] Real-time notifications (WebSocket)
- [ ] Multi-language support (i18n)
- [ ] Dark mode theme
- [ ] Account settings page
- [ ] Transaction search functionality
- [ ] Pagination for large transaction lists
- [ ] Charts/graphs for transaction analytics
- [ ] Profile management

## 📄 License

This project is part of the Money Transfer Training Application.

## 👥 Support

For issues or questions:
1. Check the troubleshooting section
2. Review backend logs for API errors
3. Check browser console for frontend errors
4. Verify all dependencies are installed correctly

---

**Built with ❤️ using Angular 17 and Angular Material**
