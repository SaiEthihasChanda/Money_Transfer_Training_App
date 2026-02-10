# JWT Authentication Implementation Guide

## Overview
This project now includes Spring Security with JWT (JSON Web Token) authentication. All API endpoints (except `/api/v1/auth/**`) require a valid JWT token in the Authorization header.

---

## Architecture

### Components

1. **JwtUtility** - Generates and validates JWT tokens
2. **JwtFilter** - Intercepts requests and validates JWT tokens
3. **SecurityConfig** - Configures Spring Security rules and filter chain
4. **AuthService** - Handles authentication logic
5. **AuthController** - Provides authentication endpoints

---

## Key Endpoints

### Public Endpoints (No Authentication Required)
- `POST /api/v1/auth/login` - Login and get JWT token
- `POST /api/v1/auth/validate` - Validate JWT token
- `GET /swagger-ui/**` - Swagger UI
- `GET /v3/api-docs/**` - API documentation

### Protected Endpoints (Require JWT Token)
- `POST /api/v1/accounts` - Create account
- `GET /api/v1/accounts` - Get all accounts
- `GET /api/v1/accounts/{id}` - Get account by ID
- `PUT /api/v1/accounts/{id}/set-password` - Set password
- `PUT /api/v1/accounts/{id}/change-password` - Change password
- `POST /api/v1/transfer` - Transfer money
- `GET /api/v1/transactions/**` - Get transactions

---

## How to Use

### Step 1: Create an Account (No Auth Needed)

This creates a User and Account together.

```bash
curl -X POST http://localhost:8080/api/v1/createaccount \
  -H "Content-Type: application/json" \
  -d '{
    "holderName": "John Doe",
    "password": "securePassword123",
    "status": "ACTIVE",
    "balance": 5000
  }'
```

Response:
```json
"Account created successfully"
```

### Step 2: Login and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "John Doe",
    "password": "securePassword123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIERvZSIsImFjY291bnRJZCI6MSwiZXhwIjoxNjExNzIzNTEwLCJpYXQiOjE2MTE3MTk5MTB9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ",
  "type": "Bearer",
  "expiresIn": 3600000
}
```

**Important**: Save the token for the next requests.

### Step 3: Use Token to Access Protected Endpoints

Add the token in the `Authorization` header with `Bearer ` prefix:

#### Get All Accounts
```bash
curl -X GET http://localhost:8080/api/v1/accounts \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIERvZSIsImFjY291bnRJZCI6MSwiZXhwIjoxNjExNzIzNTEwLCJpYXQiOjE2MTE3MTk5MTB9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

#### Get Account by ID
```bash
curl -X GET http://localhost:8080/api/v1/accounts/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2huIERvZSIsImFjY291bnRJZCI6MSwiZXhwIjoxNjExNzIzNTEwLCJpYXQiOjE2MTE3MTk5MTB9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

#### Set Password
```bash
curl -X PUT http://localhost:8080/api/v1/accounts/1/set-password \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "newPassword": "newPassword456"
  }'
```

#### Change Password
```bash
curl -X PUT http://localhost:8080/api/v1/accounts/1/change-password \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "oldPassword": "securePassword123",
    "newPassword": "newPassword456"
  }'
```

#### Transfer Money
```bash
curl -X POST http://localhost:8080/api/v1/transfer \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 500
  }'
```

---

## Using Postman

### 1. Login Request

**Method**: POST
**URL**: `http://localhost:8080/api/v1/auth/login`
**Headers**: 
- Key: `Content-Type`
- Value: `application/json`

**Body**:
```json
{
  "username": "John Doe",
  "password": "securePassword123"
}
```

Click **Send** and copy the `token` value.

### 2. Use Token for Protected Endpoints

For any protected endpoint:

**Headers**:
- Key: `Authorization`
- Value: `Bearer <YOUR_TOKEN>`

Example for getting all accounts:
- **Method**: GET
- **URL**: `http://localhost:8080/api/v1/accounts`
- **Headers**: `Authorization: Bearer eyJhbGc...`

---

## Using IntelliJ HTTP Client

Create `auth_requests.http`:

```http
### Variables
@baseUrl = http://localhost:8080/api/v1
@token = 

### Create Account (No Auth)
POST {{baseUrl}}/createaccount
Content-Type: application/json

{
  "holderName": "Test User",
  "password": "testPassword123",
  "status": "ACTIVE",
  "balance": 5000
}

### Login and Get Token
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "Test User",
  "password": "testPassword123"
}

> {% client.global.set("token", response.body.token); %}

###

### Get All Accounts (Protected)
GET {{baseUrl}}/accounts
Authorization: Bearer {{token}}

###

### Get Account by ID (Protected)
GET {{baseUrl}}/accounts/1
Authorization: Bearer {{token}}

###

### Set Password (Protected)
PUT {{baseUrl}}/accounts/1/set-password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "newPassword": "newPassword456"
}

###

### Change Password (Protected)
PUT {{baseUrl}}/accounts/1/change-password
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "oldPassword": "testPassword123",
  "newPassword": "finalPassword789"
}

###

### Validate Token
POST {{baseUrl}}/auth/validate
Authorization: Bearer {{token}}

###

### Transfer Money (Protected)
POST {{baseUrl}}/transfer
Authorization: Bearer {{token}}
Content-Type: application/json

{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 500
}
```

**To use**: Click on any request and press `Ctrl+Alt+R`

---

## JWT Token Structure

A JWT token consists of 3 parts separated by dots:

```
Header.Payload.Signature
```

### Example Token Decoded:

**Header**:
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

**Payload**:
```json
{
  "sub": "John Doe",
  "accountId": 1,
  "iat": 1611719910,
  "exp": 1611723510
}
```

**Signature**: Encrypted using the secret key

---

## Configuration

### Update JWT Secret and Expiration

Edit `application.yml`:

```yaml
jwt:
  secret: your-super-secret-key-change-in-production
  expiration: 3600000  # 1 hour in milliseconds
```

**Important**: In production, use a strong secret key and store it securely (e.g., environment variables).

---

## Error Responses

### Missing Authorization Header
```json
{
  "errorMessage": "Full authentication is required to access this resource"
}
```

### Invalid Token
```json
{
  "errorMessage": "Invalid or expired token"
}
```

### Invalid Credentials
```json
{
  "errorMessage": "Invalid username or password"
}
```

### Expired Token
```json
{
  "errorMessage": "Invalid or expired token"
}
```

---

## Security Best Practices

1. **Use HTTPS** - Always transmit tokens over HTTPS in production
2. **Strong Secret Key** - Use a strong, random secret key (at least 32 characters)
3. **Store Securely** - Never expose the secret key in code or logs
4. **Token Expiration** - Set appropriate expiration times (e.g., 1 hour for access tokens)
5. **Password Encoding** - Consider using BCryptPasswordEncoder for passwords
6. **Token Storage** - Store tokens securely on the client side (e.g., httpOnly cookies)
7. **Refresh Tokens** - For long-lived sessions, implement refresh tokens

---

## Flow Diagram

```
1. User creates account with username/password
   ↓
2. User logs in with credentials
   ↓
3. Server validates credentials
   ↓
4. Server generates JWT token
   ↓
5. Client stores token
   ↓
6. Client includes token in Authorization header for API requests
   ↓
7. Server validates token in JwtFilter
   ↓
8. Request is processed if token is valid
```

---

## Troubleshooting

### Token Always Invalid
- Check if the secret key in `application.yml` matches the one in `JwtUtility`
- Ensure token is not expired (check expiration time)
- Verify token format: `Bearer <token>`

### Cannot Login
- Verify username matches the `username` field in the users table
- Check password matches exactly (case-sensitive)
- Ensure account exists before attempting login

### 403 Forbidden on Protected Endpoints
- Check if Authorization header is included
- Verify token is valid and not expired
- Ensure endpoint is configured as protected in `SecurityConfig`

---

## Next Steps

1. Implement password encoding with BCryptPasswordEncoder
2. Add refresh token mechanism for better security
3. Implement role-based access control (RBAC)
4. Add rate limiting for login attempts
5. Implement token blacklist for logout functionality

