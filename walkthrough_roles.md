# Walkthrough - Role-Based Registration & Authentication

This update standardizes the RevPay application to use exactly three roles and provides a complete registration flow.

## Standardized Roles
The application now strictly enforces the following roles:
1.  **ADMIN:** System administrator with full oversight.
2.  **PERSONAL:** Standard personal user account.
3.  **BUSINESS:** Business account with extended features (loans, invoices).

## Functional Changes

### 1. Unified Registration Form
- Created a new **[register.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/register.html)** with a modern role selector.
- Users can choose between "Personal" and "Business" accounts during signup.
- The **[WebAuthController](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/java/com/rev/app/controller/web/WebAuthController.java)** now captures this choice and sets both the `role` and `accountType` accordingly.

### 2. Enhanced Security Context
- Updated **[SecurityConfig.java](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/java/com/rev/app/config/SecurityConfig.java)** to include role-based request matching.
- Secured routes:
    - `/pay/admin/**` -> Requires `ROLE_ADMIN`
    - `/pay/business/**` -> Requires `ROLE_BUSINESS`

### 3. Integrated Login
- Re-implemented the **[login.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/login.html)** using the premium Glassmorphism design system to match the rest of the application.

## Verification
- **Build Status:** [SUCCESS]
- **Integration:** Form submissions successfully create users with encrypted passwords and verified roles.
