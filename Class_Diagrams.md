# Class Diagrams

The following class diagrams illustrate the internal object structures across different layers of RevPay application.

## 1. Entity (Domain) Model Diagram 
*This diagram highlights the core database entities and their relationships.*

```mermaid
classDiagram
    class Auditable {
        +LocalDateTime createdAt
        +LocalDateTime lastModifiedAt
        <<MappedSuperclass>>
    }

    class User {
        +Long id
        +String email
        +String password
        +Role role
        +String twoFactorOtp
        +isTransactionAlerts()
    }
    
    class Wallet {
        +Long id
        +BigDecimal balance
    }
    
    class Transaction {
        +Long id
        +BigDecimal amount
        +TransactionType type
        +TransactionStatus status
        +String note
    }
    
    class PaymentMethod {
        +Long id
        +String cardNumber
        +String cardHolderName
        +LocalDate expiryDate
    }

    class Invoice {
        +Long id
        +BigDecimal totalAmount
        +InvoiceStatus status
    }

    Auditable <|-- User
    Auditable <|-- Wallet
    Auditable <|-- Transaction
    Auditable <|-- PaymentMethod
    Auditable <|-- Invoice

    User "1" --> "1" Wallet : owns
    User "1" --> "*" PaymentMethod : has
    User "1" --> "*" Transaction : sender/receiver
    User "1" --> "*" Invoice : issues/receives
```

## 2. Service Layer Class Diagram
*This diagram illustrates the separation of interface and implementation inside the business logic layer.*

```mermaid
classDiagram
    class IUserService {
        <<interface>>
        +registerPersonal(UserDto) UserDto
        +findByEmail(String) UserDto
        +verifyOtp(String, String) boolean
    }
    class IUserServiceImpl {
        -UserRepository userRepository
        -IEmailService emailService
        +registerPersonal(UserDto) UserDto
        +findByEmail(String) UserDto
    }
    
    class ITransactionService {
        <<interface>>
        +transferMoney(Long, Long, BigDecimal) void
        +getTransactionsForUser(Long) List
    }
    class ITransactionServiceImpl {
        -TransactionRepository txRepo
        -WalletRepository walletRepo
        +transferMoney(Long, Long, BigDecimal) void
    }

    class IEmailService {
        <<interface>>
        +sendOtpEmail(String, String)
    }
    
    IUserService <|.. IUserServiceImpl : implements
    ITransactionService <|.. ITransactionServiceImpl : implements
    IUserServiceImpl --> IEmailService : uses
    ITransactionServiceImpl --> IUserService : uses
```

## 3. Controller Layer Class Diagram
*This diagram details the REST endpoints managing incoming client HTTP requests.*

```mermaid
classDiagram
    class AuthRestController {
        -IUserService userService
        +loginUser(LoginDto) ResponseEntity
        +registerUser(UserDto) ResponseEntity
        +verify2FA(OtpDto) ResponseEntity
    }
    
    class WalletRestController {
        -IWalletService walletService
        +getWalletInfo() ResponseEntity
        +fundWallet(FundDto) ResponseEntity
    }
    
    class TransactionRestController {
        -ITransactionService txService
        +sendMoney(TransferDto) ResponseEntity
        +getHistory() ResponseEntity
    }
    
    AuthRestController --> IUserService
    WalletRestController --> IWalletService
    TransactionRestController --> ITransactionService
```
