# RevPay Cleanup and Basic UI Implementation

## Goal Description
The RevPay application currently has a non‑functional dashboard after login and contains UI elements that are overly complex for a student‑style demonstration. The goal is to clean up the codebase, ensure the dashboard loads correctly, and provide a simple, basic UI for login, registration, and dashboard pages.

## Proposed Changes
---
### Service Layer
- **UserServiceImpl**: inject `WalletRepository` and create a wallet (balance = 0) after user registration.
- **WalletServiceImpl**: ensure `createWallet` is called from `UserServiceImpl` and add null‑checks.
---
### Controller Layer
- **WebController.dashboard**: add defensive checks for missing wallet/transactions and log warnings.
---
### Thymeleaf Templates
- Create a minimal layout `layout/main-layout.html`.
- Simplify `dashboard.html`, `login.html`, `register.html` to use the layout and a single stylesheet `style.css`.
---
### Styling (`static/css/style.css`)
- Use Google Font *Inter*, light‑gray background, white cards, subtle hover effects.
---
### Security Config
- Verify `/pay/dashboard` is protected and redirects correctly after login.
---
### Tests
- Unit test for `UserServiceImpl.createUser` confirming wallet creation.
- Integration test (MockMvc) for `/pay/dashboard` returning status 200 and containing model attributes.
---
### Documentation
- Update `README.md` with overview, build/run instructions, and a brief UI guide.
- Add placeholder ERD and architecture diagram files.

## Verification Plan
### Automated Tests
- Run `mvn test` to execute all unit and integration tests.
### Manual Verification
1. Start the app (`mvn spring-boot:run`).
2. Register a new personal user, log in, and confirm the dashboard shows the user’s name, wallet balance, and recent transactions.
3. Verify the UI is simple and functional.
