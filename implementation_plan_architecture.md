# RevPay Application Complete Implementation Plan

## Goal Description
Build a full-stack monolithic financial web application named "RevPay" based on user requirements. The application supports three user roles: Admin (all operations), Business User (business + personal operations), and Personal User (personal operations only). The code will be structured cleanly to resemble a well-written student project, using an intuitive and simple UI.

## Proposed Changes
---
### 1. Database & Entities (`com.rev.app.entity`)
- `User` (Base class for shared properties: ID, email, password, role)
- `PersonalUser` (Extends User)
- `BusinessUser` (Extends User)
- `Wallet` (Linked to User)
- `Transaction` (Sender, Receiver, Amount, Type, Date, Status)
- `PaymentMethod` (Cards/Bank Accounts)
- `MoneyRequest` (Pending, Accepted, Declined)
- `Invoice` (Business only)
- `LoanApplication` (Business only)
- `Notification` (Alerts and history)

---
### 2. Repositories (`com.rev.app.repository`)
- Standard Spring Data JPA repositories for all entities.

---
### 3. Service Interfaces (`com.rev.app.service`)
- `IUserService` (Registration, Login, Profile)
- `IWalletService` (Add/Withdraw funds, Balance)
- `ITransactionService` (Transfer, History, Export)
- `IPaymentMethodService` (Add, Edit, Delete cards)
- `IMoneyRequestService` (Send, Accept, Decline)
- `IInvoiceService` (Business only: Create, Track, Mark Paid)
- `ILoanService` (Business only: Apply, Status, Repay)
- `INotificationService` (Alerts)

---
### 4. Service Implementations (`com.rev.app.service.impl`)
- Implementations for all interfaces listed above.
- Role-based logic checks (e.g., throwing exceptions if a Personal User tries to create an Invoice).

---
### 5. Controllers (`com.rev.app.controller`)
- `AuthController` (Login, Register mappings)
- `PersonalDashboardController` (Personal features)
- `BusinessDashboardController` (Business features)
- `AdminDashboardController` (Admin features)

---
### 6. Security & Configuration (`com.rev.app.config`)
- `SecurityConfig`: Configure role-based access control.
  - `/admin/**` -> Admin only
  - `/business/**` -> Admin, Business
  - `/personal/**` -> Admin, Business, Personal
  - `/auth/**` -> Permit All

---
### 7. Frontend / UI (`src/main/resources/templates`)
- Thymeleaf templates with clean, basic, student-friendly styling (HTML/CSS).
- `login.html`, `register.html`
- `dashboard-personal.html`, `dashboard-business.html`, `dashboard-admin.html`
- Fragments for header, sidebar, and footer.

## Verification Plan
### Automated Tests
- Unit tests for core services (`UserService`, `WalletService`).
- Integration tests for role-based access.

### Manual Verification
- Register and login as all three user types.
- Verify role restrictions (e.g., Personal cannot access Business pages).
- Test money transfer between two personal accounts.
- Test invoice creation for a business account.
