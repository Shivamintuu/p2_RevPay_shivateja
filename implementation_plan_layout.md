# Implementation Plan - Thymeleaf Layouts and Fragments

This plan outlines the refactoring of the RevPay frontend to use a centralized layout and reusable fragments for better maintainability and a consistent "WOW" factor.

## Proposed Changes

### [Frontend Components]

#### [NEW] [fragments/header.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/fragments/header.html)
- Contains the navigation bar, logo, and user profile summary.

#### [NEW] [fragments/sidebar.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/fragments/sidebar.html)
- Contains navigation links (Dashboard, Transactions, Send Money, Invoices, Profile).

#### [NEW] [fragments/footer.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/fragments/footer.html)
- Contains copyright and quick links.

#### [NEW] [layouts/main-layout.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/layouts/main-layout.html)
- The base template that includes common CSS/JS and slots for content.

#### [MODIFY] [dashboard.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/dashboard.html)
- Refactored to use `main-layout.html`.

#### [NEW] [transactions.html](file:///c:/Users/ADMIN/Downloads/Revpay/Revpay1/src/main/resources/templates/transactions.html)
- New page for viewing transaction history.

## Verification Plan

### Manual Verification
- Navigate to `/pay/dashboard` and verify the layout (header, sidebar) is present.
- Navigate to `/pay/transactions` and verify consistent layout.
- Verify that CSS updates in `style.css` reflect across all pages.
