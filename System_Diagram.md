# System Architecture Diagram

This document illustrates the high-level system architecture of the RevPay application platform, including components, networks, and integration points.

```mermaid
C4Context
    title System Architecture Diagram for RevPay

    Person(user, "User/Business", "A user utilizing the wallet, making payments, handling invoices.")
    Person(admin, "Admin", "System administrator overseeing compliance and support.")

    System_Boundary(c1, "RevPay Ecosystem") {
        System(webApp, "RevPay Web Application", "Provides the UI via Thymeleaf and serves APIs.")
        SystemDb(database, "RevPay Database", "Stores users, wallets, transactions, and audit logs. (Oracle/H2)")
    }

    System_Ext(emailSystem, "Email Provider (SMTP)", "Sends 2FA OTP codes and transaction alerts.")

    Rel(user, webApp, "Views dashboards, transfers money, registers", "HTTPS")
    Rel(admin, webApp, "Manages system config", "HTTPS")
    Rel(webApp, database, "Reads/Writes user and financial data", "JPA/Hibernate / JDBC")
    Rel(webApp, emailSystem, "Triggers transactional emails", "SMTP")
    Rel(emailSystem, user, "Delivers email alerts and OTPs", "Email")
```

## Component Details

### 1. Presentation & API Gateway (Web App)
- **Framework:** Spring Boot Web (Tomcat embedded).
- **Security:** Spring Security with JWT tokens for API and session-based auth for views. 2FA is validated here.
- **Role:** Routes requests, handles UI rendering with Thymeleaf, rate limiting.

### 2. Business Logic (Services)
- Coordinates multiple repositories to fulfill transactions atomically (`@Transactional`).
- Handles Email dispatch calls and DTO mapping.

### 3. Data Persistence (Database)
- Stores heavily structured relational data.
- **Framework:** Spring Data JPA with Hibernate.
- Soft-delete implemented for record preservation.

### 4. External Integrations
- **Email Service:** Utilizing Spring `JavaMailSender` connected to standard SMTP protocols to deliver OTP codes securely.
