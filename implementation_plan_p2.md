# RevPay Full Dashboard Implementation Plan

This document outlines the approach to fulfilling the complete set of Personal and Business account requirements for the RevPay application.

## User Review Required

> [!IMPORTANT]
> Since the service layer was previously regenerated as pure CRUD stubs, we must reimplement the core business logic (like handling wallet balances when a transaction occurs) before wiring up the frontend. 
> Please review this plan to ensure the order of operations and planned components align with your expectations for the P2 requirement.

## Proposed Changes

We will approach this in three major phases: **Backend Services**, **Web Controllers**, and **Frontend Views**.

### Phase 1: Backend Services (Business Logic)
We will expand the generic CRUD service implementations to include the specific business functions required by the prompt:
- **`WalletServiceImpl`**: Implement `addFunds(userId, amount, source)` and `withdrawFunds(userId, amount, destination)`.
- **`TransactionServiceImpl`**: Implement `sendMoney(senderId, receiverIdentifier, amount, note)`, which will handle deducting from sender's wallet, adding to receiver's wallet, and creating the transaction record transactionally. Implement search and filtering methods.
- **`MoneyRequestServiceImpl`**: Implement `requestMoney()`, `acceptRequest()` (which triggers a transaction), and `declineRequest()`.
- **`InvoiceServiceImpl`**: Implement invoice creation with nested items, and status transitions for Business users.
- **`LoanServiceImpl`**: Implement loan application processing and repayment tracking for Business users.

### Phase 2: Web Controllers
We will map these services to dedicated endpoints to serve the Thymeleaf templates:
- **`PersonalDashboardController`**: 
  - `GET /pay/personal/dashboard` (Stats & Summaries)
  - `GET/POST /pay/personal/transactions` (History, Filtering, Send/Request forms)
  - `GET/POST /pay/personal/wallet` (Add/Withdraw funds)
  - `GET/POST /pay/personal/cards` (Manage payment methods)
- **`BusinessDashboardController`**:
  - `GET /pay/business/dashboard` (Analytics)
  - `GET/POST /pay/business/invoices` (Manage invoices)
  - `GET/POST /pay/business/loans` (Manage loans)
  *(Business inherits all personal capabilities as well)*

### Phase 3: Frontend Views (Thymeleaf & UI)
Create and style robust HTML templates using standard Bootstrap/Custom CSS for a clean, professional look:
- **`dashboard.html`**: The main hub displaying wallet balance, recent activity, and quick action buttons.
- **`transactions.html`**: A detailed data table view with filtering options.
- **`wallet.html`**: Simple forms for simulating deposits and withdrawals.
- **`invoices.html`**: A dynamic form for creating invoices with multiple items, and a list view of existing invoices.
- **`loans.html`**: Application forms and status tracking.
- **`analytics.html`**: Visual representation of business metrics using simple HTML/CSS charts or a library like Chart.js if permitted.

## Verification Plan

### Automated Tests
- While the primary requirement is a working console/web demonstration, we will ensure that the application compiles cleanly and that core transactional logic (e.g. sending money doesn't create money out of thin air) is secure.

### Manual Verification
1. Login as a Personal User: Check wallet, send money to another user, verify balances update correctly.
2. Login as a Business User: Create an invoice, apply for a loan, and verify the analytics dashboard populates with data.
3. Validate UI responsiveness and visual aesthetics across different views.
