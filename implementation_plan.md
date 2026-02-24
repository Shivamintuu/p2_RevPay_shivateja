# DTO & Mapper Implementation Plan

To securely and effectively map the consolidated 12 entities to the Web Interface without exposing sensitive properties (like passwords, pins, etc.), we will implement a robust DTO and Mapper layer.

## Libraries
We will use **ModelMapper** as it reduces boilerplate code compared to MapStruct and integrates seamlessly with Spring Data JPA for dynamic entity mapping in smaller scale monolithic apps.

## 1. DTO Package Implementation 
Create `com.rev.app.dto` package containing records/classes for transferring data seamlessly from the controller layer.
- `UserDto` (excludes `password`)
- `PersonalUserDto` 
- `BusinessUserDto`
- `PaymentMethodDto` (excludes sensitive full card hashes, leaves masked data)
- `TransactionDto`
- `MoneyRequestDto`
- `LoanDto`
- `InvoiceDto`
- `InvoiceItemDto`
- `WalletDto`
- `NotificationDto`
*(Note: `UserSecurity` does not need a DTO as it is strictly backend validation data).*

## 2. Mapper Package Implementation
Create `com.rev.app.mapper` package. 
- Create a `MapStruct` or `ModelMapper` configuration bean (`ModelMapperConfig.java`).
- While `ModelMapper` handles 90% of basic matching, we'll create a generic `GenericMapper.java` utility component to standardize converting `Page<Entity>` to `Page<DTO>` or `List<Entity>` to `List<DTO>`.

## Execution Steps
1. Add `modelmapper` dependency to `pom.xml`.
2. Generate all the 11 DTO classes with standard validation annotations (`@NotNull`, `@Email`, etc.) since DTOs act as request payloads as well.
3. Generate the `ModelMapper` utility beans.
4. Compile using Maven to verify functionality.
