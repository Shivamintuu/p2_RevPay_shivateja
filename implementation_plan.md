# Goal Description
Update the wallet transactions (add/withdraw) to require the user's `Transaction PIN` and allow selecting a `PaymentMethod`. Additionally, automatically generate a `MoneyRequest` directed to the recipient whenever a business user generates an invoice.

## Proposed Changes

### Wallet Logic (Card Selection & PIN)
#### [MODIFY] `src/main/java/com/rev/app/rest/WalletRestController.java`
- Update `@PostMapping("/user/{userId}/add")` to accept `@RequestParam String transactionPin`.
- Update `@PostMapping("/user/{userId}/withdraw")` to accept `@RequestParam String transactionPin`.

#### [MODIFY] `src/main/java/com/rev/app/service/IWalletService.java` & `IWalletServiceImpl.java`
- Update `addFunds` and `withdrawFunds` signatures to accept `String transactionPin`.
- Inject `PasswordEncoder` and `IPaymentMethodRepository` into `IWalletServiceImpl`.
- Inside `addFunds` and `withdrawFunds`:
  - Verify the provided `transactionPin` exactly matches the user's encoded `user.getTransactionPin()` using `passwordEncoder.matches()`. Throw an `InvalidCredentialsException` or `BadRequestException` if it fails.
  - If `paymentMethodId` is provided, verify it exists and belongs to the user via repository.

#### [MODIFY] `src/main/resources/templates/wallet.html`
- Add a `<select id="paymentMethodSelect">` to the Add Funds and Withdraw Funds forms.
- On page load, `fetchWithAuth('/api/payment-methods/user/{userId}')` and populate the `<select>` options with the user's saved cards/banks.
- Add `<input type="password" id="transactionPin" required>` to both forms.
- Update the `addFunds()` and `withdrawFunds()` JavaScript functions to append `&paymentMethodId=...&transactionPin=...` to the query parameters in the POST request.

---

### Invoice Auto-Request Generation
#### [MODIFY] `src/main/java/com/rev/app/service/IInvoiceServiceImpl.java`
- Inject `IMoneyRequestService` into the constructor.
- In `createInvoice()`, immediately after `invoice = invoiceRepository.save(invoice)`, look up the `invoice.getCustomerEmail()` in the `userRepository`.
- If the customer is a valid `User` in the system, automatically call `moneyRequestService.sendRequest(businessUserId, customerUser.getId(), invoice.getTotalAmount(), "Invoice Payment: " + invoice.getInvoiceNumber())`.

## Verification Plan

### Automated Tests
- The backend tests `WalletRestControllerTest`, `IWalletServiceImplTest`, and `IInvoiceServiceImplTest` will fail if their method signatures change. We must run these tests and adapt them by mocking the `PasswordEncoder` and injecting the new `transactionPin` arguments.
- Steps to test: `mvn test -Dtest=WalletRestControllerTest` and `mvn test -Dtest=IWalletServiceImplTest` 

### Manual Verification
1. Open the application locally and log into a user account.
2. Navigate to "My Wallet" (`/wallet`).
3. View the fetched Payment Methods in the dropdown.
4. Try adding funds with an incorrect PIN to see the error, then with the correct PIN to verify success.
5. Log into a Business account. Generate an invoice directed to the personal user's email.
6. Log back into the personal user's account and check the `/money-request` or dashboard page to ensure a `MoneyRequest` was automatically created from the invoice trigger.
