# RevPay — Team of 4 Division Plan

> Each member owns a **complete vertical slice** of the application:
> Entity → Repository → Service → Controller → Thymeleaf Templates
> Everyone touches **both backend and frontend** and implements **core functionality**.

---

## 👤 Member 1 — Authentication, Security & User Profile

**Domain:** How any user gets into the system and manages their account.

### Backend
| Layer | Files |
|---|---|
| Config | `SecurityConfig.java`, `JwtAuthenticationFilter.java`, `JwtTokenProvider.java`, `UserPrincipal.java` |
| Entity | `User.java`, `Role.java` (Enum) |
| Repository | `IUserRepository.java` |
| DTOs | `UserDTO.java`, `AuthResponse.java`, `LoginRequest.java`, `RegisterRequest.java` |
| Service | `IUserService.java` / `IUserServiceImpl.java` |
| Controller | `AuthRestController.java`, `UserRestController.java` |
| Tests | `AuthRestControllerTest.java`, `IUserServiceImplTest.java` |

### Frontend (Thymeleaf)
| Template | Purpose |
|---|---|
| `index.html` | Public landing / home page |
| `login.html` | Login form with 2FA OTP support |
| `register.html` | Registration (Personal / Business role selection) |
| `profile.html` | Edit name, email, phone, and set/change Transaction PIN |
| `fragments/layout.html` | Shared nav/header layout fragment handling auth state |

### Key Features Implemented
- Spring Security 6 stateless authentication with JWT tokens
- BCrypt password hashing + separate hashed **Transaction PIN**
- Role-based URL guards (`ROLE_PERSONAL` / `ROLE_BUSINESS` / `ROLE_ADMIN`)
- Two-factor authentication via Email OTP (`AuthRestController`)
- Edit profile, change password, set/change PIN pages seamlessly integrated with JS

---

## 👤 Member 2 — Wallet, Payment Methods & Transactions

**Domain:** Moving money — adding funds, withdrawing, sending, and viewing full history.

### Backend
| Layer | Files |
|---|---|
| Entity | `Wallet.java`, `Transaction.java`, `TransactionType.java` (Enum), `PaymentMethod.java` |
| Repository | `IWalletRepository.java`, `ITransactionRepository.java`, `IPaymentMethodRepository.java` |
| DTOs | `WalletDTO.java`, `TransactionDTO.java`, `PaymentMethodDTO.java`, `SendMoneyRequest` (inner classes) |
| Service | `IWalletService.java` / `IWalletServiceImpl.java`, `ITransactionService.java` / `ITransactionServiceImpl.java`, `IPaymentMethodService.java` / `PaymentMethodServiceImpl.java` |
| Controller | `WalletRestController.java`, `TransactionRestController.java`, `PaymentMethodRestController.java` |
| Tests | `IWalletServiceImplTest.java`, `ITransactionServiceImplTest.java`, `PaymentMethodRestControllerTest.java` |

### Frontend (Thymeleaf)
| Template | Purpose |
|---|---|
| `dashboard.html` | Shared main dashboard (Wallet view, quick actions) |
| `transactions.html` | Full transaction history with filters + export |
| `payment_methods.html` | View stored linked cards and Add new methods |

### Key Features Implemented
- Wallet balance tracked and updated atomically (`@Transactional`)
- **Add Funds** logic via linked cards (simulated)
- **Send Money** — one-step debit sender + credit receiver validated by secure Transaction PIN
- Full transaction history: filter by type / status / date
- Export transaction history to **CSV** format dynamically from the backend
- Linked payment card CRUD (masking all but the last 4 digits visually)

---

## 👤 Member 3 — Money Requests, Loans & Invoices

**Domain:** Business workflows — peer money requests, EMI loans, and invoice billing.

### Backend
| Layer | Files |
|---|---|
| Entity | `MoneyRequest.java`, `LoanApplication.java`, `Invoice.java` |
| Repository | `IMoneyRequestRepository.java`, `ILoanApplicationRepository.java`, `IInvoiceRepository.java` |
| DTOs | `MoneyRequestDTO.java`, `LoanApplicationDTO.java`, `InvoiceDTO.java` |
| Service | `IMoneyRequestService.java` / `IMoneyRequestServiceImpl.java`, `ILoanApplicationService.java` / `ILoanApplicationServiceImpl.java`, `IInvoiceService.java` / `IInvoiceServiceImpl.java` |
| Controller | `MoneyRequestRestController.java`, `LoanApplicationRestController.java`, `BusinessInvoiceRestController.java` |
| Tests | `IMoneyRequestServiceImplTest.java`, `LoanApplicationRestControllerTest.java`, `IInvoiceServiceImplTest.java` |

### Frontend (Thymeleaf)
| Template | Purpose |
|---|---|
| `money_requests.html` | Create requests, Handle Incoming (Accept/Decline), view Outgoing |
| `loan_application.html` | Apply for loans and track approval statuses |
| `business_dashboard.html` | Includes the Invoice Generation UI and tracking |

### Key Features Implemented
- Request money from any RevPay user by email/phone; split view for incoming/outgoing
- **Accept** → auto-triggers `sendMoney()` flow directly from the incoming requests UI
- Pending request logic triggering backend Notification states
- Business loan application with amount, purpose, and automatic state tracking (Pending/Approved/Rejected)
- Multi-line invoice generation system for Business Users specifically

---

## 👤 Member 4 — Notifications, Analytics & Email Comm

**Domain:** Operational intelligence — in-app alerts, business dashboards, and outgoing emails.

### Backend
| Layer | Files |
|---|---|
| Entity | `Notification.java` |
| Repository | `INotificationRepository.java` |
| DTOs | `NotificationDTO.java`, `AnalyticsResponse` (Inner DTO in controller) |
| Service | `INotificationService.java` / `INotificationServiceImpl.java`, `IEmailService.java` / `EmailServiceImpl.java` |
| Controller | `NotificationRestController.java`, `BusinessAnalyticsRestController.java`, `AdminRestController.java` (if applicable) |
| Tests | `INotificationServiceImplTest.java`, `BusinessAnalyticsRestControllerTest.java` |

### Frontend (Thymeleaf)
| Template | Purpose |
|---|---|
| `notifications`/layout fragment | Dropdown badge logic inside the shared header |
| `business_dashboard.html` | Business analytics charts (Chart.js) & KPIs |
| `admin_dashboard.html` / Future | Admin-level oversight and user table management |

### Key Features Implemented
- In-app notification system (polling or on-load) showing a dynamic red badge in the Nav bar
- Complete integration of `JavaMailSender` for dispatching 2FA OTPs and Transaction Alerts via genuine SMTP
- **Business Analytics** module parsing the Transaction database to find Total Sent, Received, and rendering it to Chart.js
- (Optional/Admin) Fetching a paginated list of all users and toggling their active states from a master view.

---

## 🔁 Shared Responsibilities (All 4 Members)

| Area | Details |
|---|---|
| **Database** | Each member writes the DDL (`@Entity`) for their own tables; Hibernate auto-generates the backing H2/Oracle schema |
| **Vanilla JS** | Everyone writes logic inside `app.js` using `fetchWithAuth()` |
| **Exception Handling** | Utilizing global generic mappers or handlers where necessary |
| **Layout / CSS** | Ensure adherence to Bootstrap-style layouts and specific RevPay styling (flexbox/centers) |
| **Testing** | Execute `mvn clean test` for unit tests concerning your domain module (`Service` layer tests) |

---

## 📊 Quick Summary

| Member | Feature Area | Controllers | Entities | Core Templates |
|---|---|---|---|---|
| **1** | Auth, Profile, Security | `AuthRestController`, `UserRestController` | `User`, `Role` | `index.html`, `login.html`, `register.html`, `profile.html` |
| **2** | Wallet, Transactions, Cards | `WalletRestController`, `TransactionRestController`, `PaymentMethodRestController` | `Wallet`, `Transaction`, `PaymentMethod` | `dashboard.html`, `transactions.html`, `payment_methods.html` |
| **3** | Money Requests, Loans, Invoices | `MoneyRequestRestController`, `LoanApplicationRestController`, `BusinessInvoiceRestController` | `MoneyRequest`, `LoanApplication`, `Invoice` | `money_requests.html`, `loan_application.html` |
| **4** | Notifications, Analytics, Comm | `NotificationRestController`, `BusinessAnalyticsRestController` | `Notification` | `business_dashboard.html`, Shared Header (Notifications) |
