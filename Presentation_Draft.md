# RevPay Project Presentation

## 1. Introduction About the Project
**RevPay** is a full-stack enterprise financial web application built using Java, Spring Boot, JPA/Hibernate, and a vanilla HTML/JS/Thymeleaf frontend. It is designed to act as a comprehensive highly-secure digital wallet, seamlessly integrating personal financial management with robust business account services. The platform serves as a central ledger for money movements, supporting digital wallets, B2C billing, and lending.

## 2. Features/Uses of the App
RevPay offers two primary suite of features:

**Personal Wallet Features:**
- **Secure Authentication:** JWT-based login, password hashing (BCrypt), and Two-Factor Authentication (2FA) via Email OTP.
- **Money Transfers:** Send and request money via Email, Phone, or Account ID atomically.
- **Wallet & Payment Management:** Add/withdraw funds using linked, tokenized payment methods.
- **Transaction Tracking:** Comprehensive, filterable, and searchable transaction history.

**Enterprise & Business Features:**
- **Business Profiles:** Upgraded profiles that capture Tax IDs and business addresses.
- **Invoicing System:** Generate, send, and track payments mapping directly to ledger transactions.
- **Loan Applications:** Apply for business loans and simulate repayment/EMI mathematically.
- **Analytics Dashboard:** Graphical, real-time Chart.js integrations displaying cash flow, outstanding invoices, and revenue statistics.

## 3. Users and Their Capabilities
The application employs Role-Based Access Control (RBAC) to differentiate user capabilities:
- **Personal Users (`ROLE_PERSONAL`):** Can manage personal funds, execute peer-to-peer transfers, link payment methods, and receive invoices.
- **Business Users (`ROLE_BUSINESS`):** Inherit personal user capabilities with advanced access to issue invoices, apply for small business credit/loans, upload verification documents, and monitor business analytics.
- **Administrators (`ROLE_ADMIN`):** Have full system visibility, handle compliance verifications, and run administrative reporting.

## 4. High-Level Design (HLD)
RevPay follows a modern, monolithic **N-Tier Architecture**.
- **Frontend Layer:** UI built with Thymeleaf Templates, vanilla JS, and CSS/Bootstrap.
- **Presentation & API Layer:** 
  - *Web Controllers:* Manage server-side UI rendering, secured through HTTP Sessions.
  - *REST Controllers:* Manage API interactions for external/mobile integrations, protected by JWT tokens.
- **Service Layer (Business Logic):** Contains core financial logic decoupled from UI via interfaces (`IService`). Data structures are transferred via DTOs (Data Transfer Objects) mapping using ModelMapper.
- **Data Layer (Persistence):** Spring Data JPA with Hibernate for ORM mappings. Connects to Oracle 23ai (or H2 in-memory for testing). Transactional integrity is heavily enforced with `@Transactional`.
- **External Integration:** Connects to an external SMTP Email Provider for reliable delivery of 2FA OTP codes and transaction alerts.

## 5. Low-Level Design (LLD)
The database schema and application flow revolve around a highly structured, relational model:
- **Core Entities & Inheritance:** A base `USERS` table branches into `PERSONAL_USERS` and `BUSINESS_USERS` models. All entities inherit an `Auditable` superclass (`createdAt`, `lastModifiedAt`).
- **Core Tables:** `WALLETS`, `TRANSACTIONS`, `INVOICES`, `LOANS`, `PAYMENT_METHODS`, and `NOTIFICATIONS`.
- **Delete Strategy:** Standardized soft deletes via Hibernate `@SQLDelete` and `@SQLRestriction` to preserve financial audit trails instead of dropping rows.
- **Exception Framework:** A centralized `@ControllerAdvice` gracefully intercepts constraints, converting them to clean JSON payloads.
- **Validation:** Strict Java Bean validations on DTO payloads.

## 6. Your Contribution to the Project
My core responsibility was to build, configure, and secure the foundational authentication and authorization infrastructure. Specifically, my efforts were concentrated within three vital components:
- **`security` Package:** Engineered the core security operations. Implemented JWT generation and validation utilities, built custom security filters like the `RateLimitingFilter` (to throttle API traffic and prevent DDoS or brute-force attacks at 100 req/min/IP), and coded the `CustomUserDetails` service mapping our users to Spring Security concepts.
- **`config` Package:** Created configuration chains to protect specific application routes statically and dynamically based on roles. Configured Cross-Origin Resource Sharing (CORS) rules to secure API accessibility and structured the OpenAPI/Swagger configuration.
- **`rest` Package (Auth Controllers):** Developed the API endpoints (`AuthenticationRestController`) responsible exclusively for login flows, registration APIs, password verification hashing (via BCrypt), and integration of the 2FA Email OTP protocols during sign-in attempts.

## 7. Feature Enhancements
To scale the platform and extend its serviceability, the following enhancements could be integrated:
- **Payment Gateway Integration:** Integrate Stripe or PayPal APIs to effectively handle credit card tokenization and real money onboarding securely, rather than just simulating balances.
- **OAuth 2.0 / OIDC Integration:** Implement "Sign in with Google/GitHub" to reduce onboarding friction utilizing OAuth2 alongside standard JWTs.
- **Alternative MFA Tools:** Replace or complement Email OTP with an SMS OTP service utilizing Twilio, or Time-based One-Time Passwords (TOTP) through Google Authenticator.
- **Microservice Migration:** If consumer demand increases significantly, we could decouple the monolith—breaking out Authentication/Authorization, Wallet/Transactions, and Notifications into separate containerized (Docker/Kubernetes) Spring Boot microservices.

## 8. Conclusion
RevPay serves as an enterprise-grade financial bridge connecting individual consumers and small businesses safely and effectively. The architecture guarantees atomicity on all trades, tracks rigorous financial histories, and securely segments data based on business hierarchy. The authentication and security layers guarantee that data and currency remain strictly protected behind hashed credentials, dynamic OTPs, and cleanly decoupled service models—positioning the platform as a trusted transactional authority.
