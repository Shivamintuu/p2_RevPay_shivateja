# Use Case Diagram

The following diagram defines the primary use cases and interaction flows per actor inside RevPay.

<div align="center">

```mermaid
flowchart LR
    %% Styles
    style PersonalUser fill:#d4e6f1,stroke:#2980b9,stroke-width:2px;
    style BusinessUser fill:#f9e79f,stroke:#f1c40f,stroke-width:2px;
    style System fill:#e8daef,stroke:#8e44ad,stroke-width:2px;

    %% Actors
    PersonalUser((Personal User))
    BusinessUser((Business User))
    System((RevPay System))

    %% Use Cases
    subgraph Use Cases
        UC1([Register & Setup Wallet])
        UC2([Login with 2FA OTP])
        UC3([Send / Request Money])
        UC4([Manage Payment Methods])
        UC5([Generate Invoice])
        UC6([Apply for Business Loan])
        UC7([View Analytics Dashboard])
        UC8([Send Global System Notification])
    end

    %% Personal User Links
    PersonalUser --> UC1
    PersonalUser --> UC2
    PersonalUser --> UC3
    PersonalUser --> UC4
    UC8 -.->|Notifies| PersonalUser

    %% Business User Links (Inherits personal basics conceptually but listed here)
    BusinessUser --> UC2
    BusinessUser --> UC3
    BusinessUser --> UC5
    BusinessUser --> UC6
    BusinessUser --> UC7
    UC8 -.->|Notifies| BusinessUser

    %% System Actor Links (Automated processes)
    System -->|Triggers| UC8
    System --> UC2
```

</div>

## Actor Descriptions

1. **Personal User:** Standard user who can register, login, link cards, and send/request money to friends.
2. **Business User:** Elevated user group that can issue invoices to clients, receive payments directly to their business wallet, view financial analytics, and apply for corporate loans.
3. **RevPay System:** Automated backend processes responsible for dispatching email alerts, processing scheduled tasks, and verifying OTP keys.
