# MED.CO — Complete Backend Knowledge Base
### MCA Summer Internship Project Report — Technical Reference Document

> **Strict rule applied throughout this document:** Only code that actually exists in the repository is documented. Where a feature is absent, it is labelled **Not Implemented**.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Maven Dependencies (`pom.xml`)](#3-maven-dependencies-pomxml)
4. [Application Configuration](#4-application-configuration)
5. [Project Architecture](#5-project-architecture)
6. [Package Structure](#6-package-structure)
7. [Database Schema & Entity Documentation](#7-database-schema--entity-documentation)
8. [Data Transfer Objects (DTOs)](#8-data-transfer-objects-dtos)
9. [REST API — Controller Layer](#9-rest-api--controller-layer)
10. [Service Layer (Interfaces + Implementations)](#10-service-layer-interfaces--implementations)
11. [Repository Layer](#11-repository-layer)
12. [Security Architecture](#12-security-architecture)
13. [Authentication Flow (JWT)](#13-authentication-flow-jwt)
14. [Forgot Password / OTP / Reset Password Flow](#14-forgot-password--otp--reset-password-flow)
15. [CAPTCHA Sub-System](#15-captcha-sub-system)
16. [Exception Handling](#16-exception-handling)
17. [Utility Classes](#17-utility-classes)
18. [Scheduled Tasks](#18-scheduled-tasks)
19. [OpenAPI / Swagger Documentation](#19-openapi--swagger-documentation)
20. [Email (SMTP) Configuration](#20-email-smtp-configuration)
21. [Enumerations](#21-enumerations)
22. [Database Migration SQL](#22-database-migration-sql)
23. [Inter-Module Data Flow Diagrams](#23-inter-module-data-flow-diagrams)
24. [Implemented vs Not-Implemented Features](#24-implemented-vs-not-implemented-features)

---

## 1. Project Overview

| Attribute | Value |
|-----------|-------|
| **Application Name** | MED.CO (also referred to internally as "Health Bridge") |
| **Type** | RESTful Backend — Hospital Management System |
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.1.0 |
| **Build Tool** | Apache Maven |
| **Database** | MySQL (configured via `application.properties`) |
| **ORM** | Spring Data JPA / Hibernate |
| **Security** | Spring Security + Stateless JWT |
| **API Documentation** | SpringDoc OpenAPI (Swagger UI) |
| **Mail** | Spring Mail — Gmail SMTP |
| **CAPTCHA** | Custom Kaptcha-based image CAPTCHA |
| **PDF Generation** | OpenPDF (dependency present) |
| **Base Package** | `com.med.co` |
| **Server Port** | 8080 (default) |

### Business Domain

MED.CO is a hospital management backend that handles:

- **User Authentication** — Login, Logout (token blacklisting), Forgot/Reset Password with OTP
- **Doctor Management** — Registration, CRUD, availability scheduling (specific-date & recurring), leave management
- **Patient Management** — Registration with auto-generated MRN, CRUD
- **Appointment Management** — Booking with slot-conflict checks, cancellation, available-slot query
- **Prescription Management** — Create, retrieve by patient/doctor/appointment/MRN; HTML prescription generation
- **Billing & Invoicing** — Create invoice with automatic tax/charge calculation, reporting
- **Department Management** — CRUD + patient count per department
- **Master Record** — Auto-created on appointment booking; serves as the system-of-record linking patient, doctor, department, and appointment
- **Role-Based Access Control** — `ADMIN`, `DOCTOR`, `PATIENT` roles enforced at security layer

---

## 2. Technology Stack

| Layer / Concern | Technology / Library | Version |
|----------------|----------------------|---------|
| Core Framework | Spring Boot | 4.1.0 |
| Language | Java | 21 |
| Web Layer | Spring Web (Spring MVC) | Boot-managed |
| ORM | Spring Data JPA + Hibernate | Boot-managed |
| Database | MySQL | 8.x (driver: `mysql-connector-j`) |
| Security | Spring Security | Boot-managed |
| JWT | JJWT (io.jsonwebtoken) | 0.12.7 |
| Password Hashing | BCryptPasswordEncoder | Spring Security |
| Object Mapping | ModelMapper | 3.2.3 |
| Validation | Jakarta Bean Validation (Hibernate Validator) | Boot-managed |
| Email | Spring Mail + JavaMail | Boot-managed |
| CAPTCHA | Kaptcha (com.github.penggle) | 2.3.2 |
| API Documentation | SpringDoc OpenAPI (Swagger UI) | 2.8.8 |
| PDF Generation | OpenPDF | 1.3.30 |
| Boilerplate | Lombok | Boot-managed |
| Build | Apache Maven | 3.x |
| Test | Spring Boot Test | Boot-managed |

---

## 3. Maven Dependencies (`pom.xml`)

**Group ID:** `com.med.co`
**Artifact ID:** `MED.CO`
**Version:** `0.0.1-SNAPSHOT`
**Java Version:** `21`
**Spring Boot Parent:** `4.1.0`

### Complete Dependency List

| Dependency | Group ID | Artifact ID | Version | Scope |
|-----------|----------|-------------|---------|-------|
| Spring Web | `org.springframework.boot` | `spring-boot-starter-web` | Boot-managed | Compile |
| Spring Data JPA | `org.springframework.boot` | `spring-boot-starter-data-jpa` | Boot-managed | Compile |
| Spring Security | `org.springframework.boot` | `spring-boot-starter-security` | Boot-managed | Compile |
| Spring Mail | `org.springframework.boot` | `spring-boot-starter-mail` | Boot-managed | Compile |
| Spring Validation | `org.springframework.boot` | `spring-boot-starter-validation` | Boot-managed | Compile |
| MySQL Connector | `com.mysql` | `mysql-connector-j` | Boot-managed | Runtime |
| Lombok | `org.projectlombok` | `lombok` | Boot-managed | Compile |
| ModelMapper | `org.modelmapper` | `modelmapper` | 3.2.3 | Compile |
| JJWT API | `io.jsonwebtoken` | `jjwt-api` | 0.12.7 | Compile |
| JJWT Impl | `io.jsonwebtoken` | `jjwt-impl` | 0.12.7 | Runtime |
| JJWT Jackson | `io.jsonwebtoken` | `jjwt-jackson` | 0.12.7 | Runtime |
| SpringDoc OpenAPI | `org.springdoc` | `springdoc-openapi-starter-webmvc-ui` | 2.8.8 | Compile |
| Kaptcha | `com.github.penggle` | `kaptcha` | 2.3.2 | Compile |
| OpenPDF | `com.github.librepdf` | `openpdf` | 1.3.30 | Compile |
| Spring Boot Test | `org.springframework.boot` | `spring-boot-starter-test` | Boot-managed | Test |
| Spring Security Test | `org.springframework.security` | `spring-security-test` | Boot-managed | Test |

---

## 4. Application Configuration

### `src/main/resources/application.properties`

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/medco
spring.datasource.username=root
spring.datasource.password=<configured_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt.secret=<base64_encoded_secret>
jwt.expiration=86400000        # 24 hours in milliseconds

# SMTP (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<gmail_address>
spring.mail.password=<gmail_app_password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Key Points:**
- `ddl-auto=update` — Hibernate auto-creates/updates tables. No Flyway or Liquibase is used.
- SQL logging is enabled (`show-sql=true`) — useful for development but should be disabled in production.
- JWT expiration is set to **24 hours** (86,400,000 ms).
- Gmail SMTP uses **STARTTLS** on port **587** with App Password authentication.

> **Not Implemented:** `application.yml` — the project uses only `application.properties`.

---

## 5. Project Architecture

### Architectural Pattern

The project follows the classic **Layered (N-Tier) Architecture** for Spring Boot:

```text
Client (Angular / Postman / Swagger UI)
        |
        | HTTP Request (JSON)
        v
JwtAuthFilter (validates JWT, blocks revoked tokens)
Spring Security FilterChain
        |
        v
Controller Layer  (@RestController)
Handles HTTP method mapping, request/response serialisation
        |
        | calls service
        v
Service Layer  (@Service interface + Impl)
Business logic, validation, transaction management
        |
        | calls repository
        v
Repository Layer  (JpaRepository extensions)
Database CRUD, JPQL custom queries
        |
        | ORM
        v
MySQL Database  (medco schema)
```

### Cross-Cutting Concerns

| Concern | Implementation |
|---------|---------------|
| Authentication | `JwtAuthFilter` + `CustomUserDetailsService` |
| Authorisation | `SecurityConfig` — role-based URL access |
| Global Exception Handling | `GlobalExceptionHandler` (`@RestControllerAdvice`) |
| DTO Mapping | `ModelMapper` (bean) |
| Logging | SLF4J + Logback (default Spring Boot); `@Slf4j` Lombok annotation |
| Scheduled Jobs | Spring `@Scheduled` (cron-based) |
| Encryption | `EncryptionUtil` — AES/CBC/PKCS5Padding |
| CAPTCHA | `CaptchaServiceImpl` + `CaptchaGenerator` + in-memory `ConcurrentHashMap` |
| API Docs | SpringDoc OpenAPI / Swagger UI |

---

## 6. Package Structure

```text
com.med.co
+-- MedCoApplication.java               <- Spring Boot entry point
+-- config/
|   +-- AppConfig.java                  <- ModelMapper bean + PasswordEncoder bean
|   +-- MailConfig.java                 <- JavaMailSender configuration
|   +-- OpenApiConfig.java              <- Swagger / OpenAPI configuration
+-- controller/
|   +-- AppointmentController.java
|   +-- AuthController.java
|   +-- BillingInvoiceController.java
|   +-- DepartmentController.java
|   +-- DoctorAvailabilityController.java
|   +-- DoctorController.java
|   +-- DoctorLeaveController.java
|   +-- MasterController.java
|   +-- PatientController.java
|   +-- PrescriptionController.java
+-- dto/
|   +-- request/                        <- Inbound DTOs
|   +-- response/                       <- Outbound DTOs
+-- entity/
|   +-- Appointment.java
|   +-- BillingInvoice.java
|   +-- Department.java
|   +-- Doctor.java
|   +-- DoctorAvailability.java
|   +-- DoctorLeave.java
|   +-- DoctorRecurringAvailability.java
|   +-- Master.java
|   +-- PasswordResetOtp.java
|   +-- Patient.java
|   +-- Prescription.java
|   +-- RevokedToken.java
|   +-- Role.java
|   +-- UserRole.java
+-- enums/
|   +-- AppointmentStatus.java
|   +-- Enums.java                      <- RoleType, DoctorStatus, Gender, BloodGroup, etc.
|   +-- LeaveStatus.java
+-- exception/
|   +-- BadRequestException.java
|   +-- GlobalExceptionHandler.java
|   +-- ResourceAlreadyExistsException.java
|   +-- ResourceNotFoundException.java
+-- repository/
|   +-- AppointmentRepository.java
|   +-- BillingInvoiceRepository.java
|   +-- DepartmentRepository.java
|   +-- DoctorAvailabilityRepository.java
|   +-- DoctorLeaveRepository.java
|   +-- DoctorRecurringAvailabilityRepository.java
|   +-- DoctorRepository.java
|   +-- MasterRepository.java
|   +-- PasswordResetOtpRepository.java
|   +-- PatientRepository.java
|   +-- PrescriptionRepository.java
|   +-- RevokedTokenRepository.java
|   +-- RoleRepository.java
|   +-- UserRepository.java
+-- security/
|   +-- AuthController.java             <- (moved to controller package)
|   +-- AuthService.java / AuthServiceImpl.java
|   +-- CaptchaCache.java
|   +-- CaptchaGenerator.java
|   +-- CustomUserDetailsService.java
|   +-- JwtAuthFilter.java
|   +-- JwtUtils.java
|   +-- SecurityConfig.java
+-- service/                            <- Service interfaces
+-- serviceimpl/                        <- Service implementations
+-- util/
    +-- EncryptionUtil.java
    +-- PrescriptionHtmlGenerator.java
```

---

## 7. Database Schema & Entity Documentation

> All tables are auto-created/updated by Hibernate (`ddl-auto=update`). The database name is **medco**.

---

### 7.1 `role` Table — `Role.java`

Stores the system roles.

| Column | Java Field | Type | Constraints |
|--------|-----------|------|-------------|
| `role_id` | `roleId` | `Long` | PK, Auto-increment |
| `role_name` | `roleName` | `RoleType` (Enum) | NOT NULL |

**Enum Values for RoleType:** `ADMIN`, `DOCTOR`, `PATIENT`

---

### 7.2 `user_role` Table — `UserRole.java`

The central authentication entity. Stores login credentials and links to the `role` table.

| Column | Java Field | Type | Constraints |
|--------|-----------|------|-------------|
| `id` | `id` | `Long` | PK, Auto-increment |
| `email` | `email` | `String` | NOT NULL, UNIQUE |
| `password` | `password` | `String` | NOT NULL (BCrypt hashed) |
| `enabled` | `enabled` | `boolean` | NOT NULL |
| `role_id` (FK) | `role` | `Role` | ManyToOne -> `role.role_id` |

**Relationship:**
- `UserRole` -> `Role`: `@ManyToOne` (each user has exactly one role)
- `Doctor` -> `UserRole`: `@OneToOne` (optional linkage)
- `Patient` -> `UserRole`: `@OneToOne`

---

### 7.3 `doctor` Table — `Doctor.java`

Stores all doctor personal, professional, and contact details.

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `id` | `id` | `Long` | PK |
| `first_name` | `firstName` | `String` | |
| `middle_name` | `middleName` | `String` | Nullable |
| `last_name` | `lastName` | `String` | |
| `gender` | `gender` | `Enum` | `MALE`, `FEMALE`, `OTHER` |
| `date_of_birth` | `dateOfBirth` | `LocalDate` | |
| `blood_group` | `bloodGroup` | `Enum` | |
| `nationality` | `nationality` | `String` | |
| `mobile_number` | `mobileNumber` | `String` | UNIQUE |
| `alternate_mobile_number` | `alternateMobileNumber` | `String` | Nullable |
| `email` | `email` | `String` | Stored lowercase |
| `address` | `address` | `String` | |
| `city` | `city` | `String` | |
| `state` | `state` | `String` | |
| `country` | `country` | `String` | |
| `pin_code` | `pinCode` | `String` | |
| `medical_registration_number` | `medicalRegistrationNumber` | `String` | UNIQUE |
| `qualification` | `qualification` | `String` | |
| `specialization` | `specialization` | `String` | |
| `experience` | `experience` | `int` | Years |
| `designation` | `designation` | `String` | |
| `department_id` (FK) | `department` | `Department` | ManyToOne |
| `status` | `status` | `DoctorStatus` Enum | Default: `AVAILABLE` |

**Relationships:**
- `Doctor` -> `Department`: `@ManyToOne`
- `Doctor` -> `DoctorAvailability`: `@OneToMany` (implicitly via doctorId FK)
- `Doctor` -> `Appointment`: `@OneToMany`
- `Doctor` -> `DoctorLeave`: `@OneToMany`

---

### 7.4 `patient` Table — `Patient.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `patient_id` | `patientId` | `Long` | PK |
| `mrn_no` | `mrnNo` | `String` | UNIQUE; auto-generated format `MRN` + 6-digit random number |
| `first_name` | `firstName` | `String` | |
| `last_name` | `lastName` | `String` | |
| `date_of_birth` | `dateOfBirth` | `LocalDate` | Used to compute age in prescriptions |
| `address` | `address` | `String` | |
| `userrole_id` (FK) | `userrole` | `UserRole` | `@OneToOne` |

**MRN Generation Logic:**
```java
do {
    mrnNo = "MRN" + (100000 + random.nextInt(900000));  // e.g., MRN847293
} while (patientRepository.existsByMrnNo(mrnNo));
```

---

### 7.5 `department` Table — `Department.java`

| Column | Java Field | Type | Constraints |
|--------|-----------|------|-------------|
| `department_id` | `departmentId` | `Long` | PK |
| `department_name` | `departmentName` | `String` | UNIQUE |

---

### 7.6 `appointment` Table — `Appointment.java`

Core transactional entity. Manages appointment lifecycle.

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `appointment_id` | `appointmentId` | `Long` | PK |
| `doctor_id` (FK) | `doctor` | `Doctor` | `@ManyToOne` |
| `patient_id` (FK) | `patient` | `Patient` | `@ManyToOne` |
| `appointment_date` | `appointmentDate` | `LocalDate` | |
| `appointment_time` | `appointmentTime` | `LocalTime` | |
| `reason` | `reason` | `String` | Reason for visit |
| `status` | `status` | `AppointmentStatus` Enum | `SCHEDULED`, `COMPLETED`, `CANCELLED` |
| `created_at` | `createdAt` | `LocalDateTime` | Set by `@PrePersist` |
| `updated_at` | `updatedAt` | `LocalDateTime` | Set by `@PreUpdate` |

**@PrePersist / @PreUpdate Hooks:**
```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (status == null) status = AppointmentStatus.SCHEDULED;
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

---

### 7.7 `prescription` Table — `Prescription.java`

Linked 1:1 to an Appointment.

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `prescription_id` | `prescriptionId` | `Long` | PK |
| `appointment_id` (FK) | `appointment` | `Appointment` | `@ManyToOne` |
| `doctor_id` (FK) | `doctor` | `Doctor` | `@ManyToOne` |
| `patient_id` (FK) | `patient` | `Patient` | `@ManyToOne` |
| `diagnosis` | `diagnosis` | `String` | |
| `medicines` | `medicines` | `String` | Free-text medicine list |
| `advice` | `advice` | `String` | Doctor's advice |
| `prescription_html` | `prescriptionHtml` | `@Lob String` | Full HTML prescription document |
| `created_at` | `createdAt` | `LocalDateTime` | `@PrePersist` |

**Constraint:** Only one prescription per appointment (enforced by `existsByAppointment` check in service).

---

### 7.8 `billing_invoice` Table — `BillingInvoice.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `invoice_id` | `invoiceId` | `Long` | PK |
| `patient_id` | `patientId` | `Long` | Stored as raw Long (not FK) |
| `patient_name` | `patientName` | `String` | |
| `invoice_date` | `invoiceDate` | `LocalDate` | |
| `invoice_number` | `invoiceNumber` | `String` | UNIQUE; format: `INV-YYYYMMDD-0001` |
| `total_amount` | `totalAmount` | `BigDecimal` | Stored with scale 2 |

**Billing Calculation:**
- `medicineTotal` = `unitPrice x quantity` (per medicine)
- `medicineSubtotal` = sum of all `medicineTotal`
- `doctorSubtotal` = doctor's consultation fee
- `serviceCharge` = **fixed Rs. 10.00**
- `subtotalBeforeTax` = `doctorSubtotal + medicineSubtotal + serviceCharge`
- `tax` = **5%** of `subtotalBeforeTax`
- `totalAmount` = `doctorSubtotal + medicineSubtotal + serviceCharge + tax`

---

### 7.9 `master` Table — `Master.java`

Acts as a central ledger. Created automatically whenever an appointment is booked.

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `master_id` | `masterId` | `Long` | PK |
| `mrn_no` | `mrnNo` | `String` | Patient's MRN |
| `patient_id` (FK) | `patient` | `Patient` | |
| `doctor_id` (FK) | `doctor` | `Doctor` | |
| `department_id` (FK) | `department` | `Department` | |
| `appointment_id` (FK) | `appointment` | `Appointment` | |
| `appointment_date` | `appointmentDate` | `LocalDate` | |
| `appointment_time` | `appointmentTime` | `LocalTime` | |
| `status` | `status` | `String` | Appointment status text |

**Creation Trigger:** `AppointmentServiceImpl.bookAppointment()` calls `masterService.createMasterRecord(appointment)` upon successful booking.

---

### 7.10 `doctor_availability` Table — `DoctorAvailability.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `availability_id` | `availabilityId` | `Long` | PK |
| `doctor_id` (FK) | `doctor` | `Doctor` | |
| `available_date` | `availableDate` | `LocalDate` | |
| `start_time` | `startTime` | `LocalTime` | |
| `end_time` | `endTime` | `LocalTime` | |
| `available` | `available` | `Boolean` | Default: `true` |

---

### 7.11 `doctor_recurring_availability` Table — `DoctorRecurringAvailability.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `recurring_availability_id` | `id` | `Long` | PK |
| `doctor_id` (FK) | `doctor` | `Doctor` | FK with CASCADE DELETE |
| `recurrence_type` | `recurrenceType` | `RecurrenceType` Enum | `MONTHLY_BY_DAY`, `WEEKLY_BY_DAY`, `DAILY` |
| `day_of_month` | `dayOfMonth` | `Integer` | For `MONTHLY_BY_DAY` (1-31) |
| `day_of_week` | `dayOfWeek` | `Integer` | For `WEEKLY_BY_DAY` (1=Mon, 7=Sun) |
| `start_time` | `startTime` | `LocalTime` | |
| `end_time` | `endTime` | `LocalTime` | |
| `start_date` | `startDate` | `LocalDate` | Nullable — effective from date |
| `end_date` | `endDate` | `LocalDate` | Nullable — effective until date |
| `active` | `active` | `Boolean` | Default: `true` |
| `created_at` | `createdAt` | `LocalDateTime` | |
| `updated_at` | `updatedAt` | `LocalDateTime` | |

---

### 7.12 `doctor_leave` Table — `DoctorLeave.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `leave_id` | `leaveId` | `Long` | PK |
| `doctor_id` (FK) | `doctor` | `Doctor` | |
| `leave_type` | `leaveType` | `String` | |
| `from_date` | `fromDate` | `LocalDate` | |
| `to_date` | `toDate` | `LocalDate` | |
| `reason` | `reason` | `String` | |
| `status` | `status` | `LeaveStatus` Enum | Default: `PENDING`; Values: `PENDING`, `APPROVED`, `REJECTED`, `COMPLETED` |

**Automatic Status Update:** `LeaveSchedulerServiceImpl` runs daily at 6:00 AM and transitions `APPROVED` leaves whose `toDate` is in the past to `COMPLETED`.

---

### 7.13 `password_reset_otp` Table — `PasswordResetOtp.java`

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `id` | `id` | `Long` | PK |
| `email` | `email` | `String` | |
| `otp` | `otp` | `String` | 6-digit OTP |
| `created_at` | `createdAt` | `LocalDateTime` | |
| `expires_at` | `expiresAt` | `LocalDateTime` | 5 minutes after creation |
| `used` | `used` | `boolean` | Marks OTP as consumed |

---

### 7.14 `revoked_token` Table — `RevokedToken.java`

Implements JWT token blacklisting for logout support.

| Column | Java Field | Type | Notes |
|--------|-----------|------|-------|
| `id` | `id` | `Long` | PK |
| `token` | `token` | `String` | Full JWT string |

**Usage:** `JwtAuthFilter` checks `revokedTokenRepository.existsByToken(token)` on every request. `AuthServiceImpl.logout()` saves the token to this table.

---

## 8. Data Transfer Objects (DTOs)

### 8.1 Request DTOs (`dto/request/`)

| Class | Purpose | Key Fields |
|-------|---------|-----------|
| `LoginRequest` | User login | `email`, `password`, `captchaId`, `captchaText` |
| `ForgotPasswordRequest` | Initiate OTP flow | `email` |
| `VerifyOtpRequest` | Validate OTP | `email`, `otp` |
| `ResetPasswordRequest` | Set new password | `email`, `otp`, `newPassword` |
| `DoctorRegistrationRequest` | Register doctor | All doctor fields + `password`, `departmentId` |
| `DoctorUpdateRequest` | Update doctor | All updatable doctor fields |
| `PatientRegistrationRequest` | Register/update patient | All patient fields + `password` |
| `AppointmentRequestDto` | Book appointment | `doctorId`, `patientId`, `appointmentDate`, `appointmentTime`, `reason` |
| `PrescriptionRequestDto` | Create prescription | `appointmentId`, `diagnosis`, `medicines`, `advice` |
| `BillingInvoiceRequestDto` | Create invoice | `patientId`, `patientName`, `invoiceDate`, `doctorConsultation`, `medicines[]` |
| `DateRangeBillingRequestDto` | Billing by date range | `fromDate`, `toDate` |
| `DoctorAvailabilityRequest` | Add/update availability | `doctorId`, `availableDate`, `startTime`, `endTime`, `available` |
| `DoctorLeaveRequestDto` | Apply for leave | `doctorId`, `leaveType`, `fromDate`, `toDate`, `reason` |
| `LeaveStatusRequestDto` | Approve/reject leave | `status` (LeaveStatus) |
| `DepartmentRequest` | Create/update dept | `departmentName` |
| `DepartmentPatientCountRequest` | Count patients by dept | `departmentName` |
| `MedicineRequestDto` | Medicine item in invoice | `medicineName`, `quantity`, `unitPrice` |

### 8.2 Response DTOs (`dto/response/`)

| Class | Purpose | Key Fields |
|-------|---------|-----------|
| `ApiResponse<T>` | Universal wrapper | `status` (int), `message` (String), `data` (T) |
| `LoginResponse` | Login result | `token`, `email`, `role` |
| `DoctorResponseDto` | Doctor details | All doctor fields + `departmentId`, `departmentName` |
| `PatientResponseDto` | Patient details | All patient fields |
| `AppointmentResponseDto` | Appointment details | `appointmentId`, `doctorId`, `patientId`, `date`, `time`, `status`, etc. |
| `PrescriptionResponseDto` | Prescription details | `prescriptionId`, `appointmentId`, `doctorId`, `patientId`, `mrnNo`, `diagnosis`, `medicines`, `advice`, `prescriptionHtml`, `createdAt` |
| `BillingInvoiceResponseDto` | Invoice details | `id`, `invoiceNumber`, `patientId`, `patientName`, `invoiceDate`, `doctorConsultation`, `medicines[]`, `doctorSubtotal`, `medicineSubtotal`, `serviceCharge`, `tax`, `totalAmount` |
| `MonthlyBillingSummaryDto` | Month billing roll-up | `startDate`, `endDate`, `totalInvoicesCount`, `totalAmount`, `invoices[]` |
| `DateRangeBillingSummaryDto` | Date-range billing | `fromDate`, `toDate`, `totalAmount` |
| `DoctorLeaveResponseDto` | Leave details | `leaveId`, `doctorId`, `doctorName`, `leaveType`, `fromDate`, `toDate`, `reason`, `status` |
| `DepartmentResponse` | Department details | `departmentId`, `departmentName` |
| `CaptchaResponse` | CAPTCHA response | `captchaId`, `captchaImage` (Base64) |
| `AvailableDoctorsCountResponse` | Available doctors count | `date`, `availableCount`, `totalScheduledRecurring`, `totalScheduledSpecific`, `totalOnLeaveApproved`, `message` |
| `MedicineResponseDto` | Medicine in invoice | `medicineName`, `quantity`, `unitPrice`, `medicineTotal` |
| `DoctorConsultationResponseDto` | Consultation in invoice | `doctorName`, `specialization`, `doctorFee` |
| `DepartmentPatientCountResponse` | Patient count by dept | `departmentName`, `patientCount` |

---

## 9. REST API — Controller Layer

Base URL: `http://localhost:8080`

### 9.1 Authentication Controller — `/api/auth`

**Class:** `AuthController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/auth")`, `@CrossOrigin("*")`

| Method | Endpoint | Auth Required | Description |
|--------|---------|---------------|-------------|
| `POST` | `/api/auth/login` | No | Authenticates user; returns JWT |
| `GET` | `/api/auth/create-captcha` | No | Generates CAPTCHA image |
| `POST` | `/api/auth/forgot-password` | No | Triggers OTP email |
| `POST` | `/api/auth/verify-otp` | No | Validates OTP |
| `POST` | `/api/auth/reset-password` | No | Resets password with valid OTP |
| `POST` | `/api/auth/logout` | Yes (JWT) | Blacklists current JWT token |

---

### 9.2 Doctor Controller — `/api/doctors`

**Class:** `DoctorController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/doctors")`, `@CrossOrigin("*")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/doctors/register` | Register a new doctor (creates Doctor + UserRole) |
| `GET` | `/api/doctors` | Get all doctors (paginated + sorted) |
| `GET` | `/api/doctors/{id}` | Get doctor by ID |
| `PUT` | `/api/doctors/{id}` | Update doctor details |
| `DELETE` | `/api/doctors/{id}` | Delete doctor |
| `PUT` | `/api/doctors/leave/{leaveId}/status` | Update leave status (NOT IMPLEMENTED — returns null) |
| `GET` | `/api/doctors/count` | Count total doctors |
| `GET` | `/api/doctors/department/{departmentId}` | Get doctors by department |

**Pagination parameters for GET /api/doctors:**
`page` (default 0), `size` (default 10), `sortBy` (default `doctorId`), `direction` (`asc`/`desc`)

---

### 9.3 Patient Controller — `/api/patient`

**Class:** `PatientController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/patient")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/patient/register` | Register patient (creates Patient + UserRole) |
| `GET` | `/api/patient/all` | Get all patients (paginated + sorted) |
| `GET` | `/api/patient/{id}` | Get patient by ID |
| `GET` | `/api/patient/mrn/{mrnNo}` | Get patient by MRN number |
| `PUT` | `/api/patient/update/{id}` | Update patient details |
| `DELETE` | `/api/patient/delete/{id}` | Delete patient + linked UserRole |
| `GET` | `/api/patient/count` | Count total patients |
| `POST` | `/api/patient/count-by-department` | Count patients by department |

---

### 9.4 Appointment Controller — `/api/appointments`

**Class:** `AppointmentController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/appointments")`, `@CrossOrigin("http://localhost:4200")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/appointments/book` | Book an appointment (with conflict validation) |
| `PUT` | `/api/appointments/cancel/{appointmentId}` | Cancel appointment |
| `GET` | `/api/appointments/get/{appointmentId}` | Get appointment by ID |
| `GET` | `/api/appointments/doctor/get/{doctorId}` | Get all appointments of a doctor |
| `GET` | `/api/appointments/patient/get/{patientId}` | Get all appointments of a patient |
| `GET` | `/api/appointments/available-dates/get/{doctorId}` | Get available dates for a doctor |
| `GET` | `/api/appointments/available-slots/get?doctorId=&appointmentDate=` | Get available time slots |
| `GET` | `/api/appointments/count/today` | Count today's appointments |

---

### 9.5 Prescription Controller — `/api/prescriptions`

**Class:** `PrescriptionController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/prescriptions")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/prescriptions/create` | Create prescription (generates HTML) |
| `GET` | `/api/prescriptions/get/{prescriptionId}` | Get by prescription ID |
| `GET` | `/api/prescriptions/appointment/get/{appointmentId}` | Get by appointment ID |
| `GET` | `/api/prescriptions/mrn/{mrnNo}` | Get by patient MRN number |
| `GET` | `/api/prescriptions/patient/{patientId}` | Get all prescriptions of a patient |
| `GET` | `/api/prescriptions/doctor/get/{doctorId}` | Get all prescriptions by a doctor |
| `GET` | `/api/prescriptions/getAll` | Get all prescriptions |

---

### 9.6 Billing Invoice Controller — `/api/billing`

**Class:** `BillingInvoiceController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/billing")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/billing/create/invoice` | Create billing invoice with tax calculation |
| `GET` | `/api/billing/calculate/current-month` | Monthly billing summary (1st to today) |
| `POST` | `/api/billing/calculate/by-date-range` | Billing summary between two dates |
| `GET` | `/api/billing/all-invoices` | Retrieve all invoices |
| `GET` | `/api/billing/show/using-invoiceNumber?invoiceNumber=` | Find by invoice number |
| `GET` | `/api/billing/show/using-invoiceDate?invoiceDate=` | Find by invoice date |
| `GET` | `/api/billing/show-bill/id-wise/{id}` | Get invoice by ID |
| `DELETE` | `/api/billing/delete/id-wise/{id}` | Delete invoice |

---

### 9.7 Department Controller — `/api/departments`

**Class:** `DepartmentController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/departments")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/departments/add-dept` | Add new department |
| `GET` | `/api/departments/getAll` | Get all departments |
| `GET` | `/api/departments/get/{id}` | Get department by ID |
| `PUT` | `/api/departments/update/{id}` | Update department name |
| `DELETE` | `/api/departments/delete/{id}` | Delete department |
| `GET` | `/api/departments/count` | Total department count |

---

### 9.8 Doctor Availability Controller — `/api/doctor-availability`

**Class:** `DoctorAvailabilityController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/doctor-availability")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/doctor-availability/create` | Add availability slot |
| `GET` | `/api/doctor-availability/get/{doctorId}` | Get all availability by doctor |
| `PUT` | `/api/doctor-availability/update/{availabilityId}` | Update availability |
| `DELETE` | `/api/doctor-availability/delete/{availabilityId}` | Delete availability |
| `GET` | `/api/doctor-availability/available/count` | Count available doctors on a date |

---

### 9.9 Doctor Leave Controller — `/api/doctor-leaves`

**Class:** `DoctorLeaveController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/doctor-leaves")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/doctor-leaves/apply` | Apply for leave |
| `GET` | `/api/doctor-leaves/view/{leaveId}` | Get leave by ID |
| `GET` | `/api/doctor-leaves/view/all` | Get all leaves |
| `GET` | `/api/doctor-leaves/view/{doctorId}` | Get leaves by doctor |
| `PUT` | `/api/doctor-leaves/own-update/{leaveId}` | Update leave dates/reason |
| `DELETE` | `/api/doctor-leaves/delete/{leaveId}` | Delete leave |
| `PUT` | `/api/doctor-leaves/admin/{leaveId}/status` | Approve/Reject leave |
| `GET` | `/api/doctor-leaves/all-leave/count/date-wise` | Count doctors on leave on a date |
| `GET` | `/api/doctor-leaves/count/total` | Total leave records |
| `GET` | `/api/doctor-leaves/count/status-wise` | Count by status |
| `GET` | `/api/doctor-leaves/count/active-leaves` | Count active (ongoing) leaves |

---

### 9.10 Master Controller — `/api/master`

**Class:** `MasterController.java`
**Annotations:** `@RestController`, `@RequestMapping("/api/master")`

| Method | Endpoint | Description |
|--------|---------|-------------|
| `GET` | `/api/master/getAll` | Get all master records |
| `GET` | `/api/master/get/{id}` | Get master record by ID |
| `GET` | `/api/master/get/mrn/{mrnNo}` | Get master records by MRN |
| `DELETE` | `/api/master/delete/{id}` | Delete master record |

> **Note:** Master records are created automatically by `AppointmentServiceImpl.bookAppointment()`.

---

## 10. Service Layer (Interfaces + Implementations)

### 10.1 AuthService / AuthServiceImpl

| Method | Logic Summary |
|--------|--------------|
| `login(LoginRequest)` | 1. Validates CAPTCHA via `CaptchaService.validateCaptcha()`; 2. Decrypts password using `EncryptionUtil.decrypt()`; 3. Loads user via `CustomUserDetailsService`; 4. Verifies BCrypt password; 5. Generates JWT via `JwtUtils.generateToken()`; 6. Returns `LoginResponse` with token, email, role |
| `forgotPassword(ForgotPasswordRequest)` | 1. Checks if email exists in `UserRepository`; 2. Deletes old OTPs for the email; 3. Generates 6-digit OTP; 4. Saves to `PasswordResetOtp` with 5-min expiry; 5. Sends OTP email via `EmailService` |
| `verifyOtp(VerifyOtpRequest)` | 1. Fetches latest OTP record for email; 2. Checks if not used and not expired; 3. Returns success/failure |
| `resetPassword(ResetPasswordRequest)` | 1. Verifies OTP is valid and not used; 2. Encodes new password with BCrypt; 3. Updates `UserRole.password`; 4. Marks OTP as used |
| `logout(HttpServletRequest)` | 1. Extracts Bearer token from `Authorization` header; 2. Saves token to `RevokedToken` table; 3. Returns success message |

---

### 10.2 DoctorService / DoctorServiceImpl

| Method | Key Logic |
|--------|----------|
| `registerDoctor(request)` | Validates unique email, mobile, medical reg. number; creates `Doctor` + `UserRole` in `@Transactional`; sets initial status to `AVAILABLE` |
| `getDoctorById(id)` | Fetches doctor or throws `ResourceNotFoundException` |
| `getAllDoctors(page, size, sortBy, direction)` | Paginated + sorted via `PageRequest` |
| `getDoctorsByDepartment(departmentId)` | Queries by `department.departmentId` |
| `updateDoctor(id, request)` | Validates duplicates excluding self; updates all fields |
| `deleteDoctor(id)` | Deletes doctor entity |
| `updateLeaveStatus(leaveId, request)` | **NOT IMPLEMENTED** — returns `null` (TODO in code) |
| `getTotalDoctors()` | Returns `doctorRepository.count()` |

**Private helper:** `mapDoctorToResponse(Doctor)` — uses `ModelMapper` then manually sets `departmentId` and `departmentName`.

---

### 10.3 PatientService / PatientServiceImpl

| Method | Key Logic |
|--------|----------|
| `registerPatient(request)` | Checks email uniqueness; creates `UserRole` first, then creates `Patient` and links them; auto-generates MRN |
| `getAllPatients(page, size, sortBy, direction)` | Paginated + sorted |
| `getPatientById(id)` | Fetch by ID or 404 |
| `getPatientByMrn(mrnNo)` | Lookup by MRN |
| `updatePatient(id, request)` | Updates both `Patient` and linked `UserRole`; encodes new password if provided |
| `countAllPatients()` | Returns `patientRepository.count()` |
| `countPatientsByDepartment(request)` | Uses custom JPQL query counting distinct patients who had appointments in the department |
| `deletePatient(id)` | Deletes `Patient` then linked `UserRole` |

---

### 10.4 AppointmentService / AppointmentServiceImpl

**bookAppointment() Detailed Flow:**

```text
1. Fetch Doctor by doctorId  -> throws ResourceNotFoundException if not found
2. Fetch Patient by patientId -> throws ResourceNotFoundException if not found
3. Check if doctor is on APPROVED leave on appointmentDate
   -> throws BadRequestException: "Doctor is on leave on this date"
4. Check if DoctorAvailability exists for doctorId + appointmentDate + available=true
   -> throws BadRequestException: "Doctor is not available on this date"
5. Check if time slot is already booked (SCHEDULED status)
   -> throws BadRequestException: "Time slot already booked"
6. Create and save Appointment (status = SCHEDULED)
7. Call masterService.createMasterRecord(savedAppointment)
   -> creates Master record linking patient, doctor, dept, appointment
8. Map and return AppointmentResponseDto
```

| Method | Key Logic |
|--------|----------|
| `bookAppointment(request)` | Full conflict-checked booking with master record creation |
| `cancelAppointment(id)` | Sets status to `CANCELLED` |
| `getAppointmentById(id)` | Fetch by ID |
| `getAppointmentsByDoctor(doctorId)` | Fetch by Doctor entity |
| `getAppointmentsByPatient(patientId)` | Fetch by Patient entity |
| `getAvailableDates(doctorId)` | Returns list of LocalDate where doctor has available=true |
| `getAvailableTimeSlots(doctorId, date)` | Computes free slots by subtracting booked slots |
| `countAppointmentsToday()` | `countByAppointmentDate(LocalDate.now())` |

---

### 10.5 PrescriptionService / PrescriptionServiceImpl

| Method | Key Logic |
|--------|----------|
| `createPrescription(requestDto)` | Validates appointment; ensures no duplicate; generates HTML via `PrescriptionHtmlGenerator`; saves and returns DTO |
| `getPrescriptionById(id)` | Fetch by ID |
| `getPrescriptionByAppointment(appointmentId)` | Fetch appointment then linked prescription |
| `getPrescriptionByMrnNo(mrnNo)` | Find by `patient.mrnNo` |
| `getPrescriptionsByPatient(patientId)` | List by `patient.patientId` |
| `getPrescriptionsByDoctor(doctorId)` | List by `doctor.id` |
| `getAllPrescriptions()` | `findAll()` |

---

### 10.6 BillingInvoiceService / BillingInvoiceServiceImpl

| Method | Key Logic |
|--------|----------|
| `createBillingInvoice(request)` | Computes all billing amounts; generates unique invoice number; saves to DB; returns full breakdown DTO |
| `getBillingInvoiceById(invoiceId)` | Fetch by ID |
| `getBillingInvoiceByInvoiceNumber(invoiceNumber)` | Fetch by invoice number |
| `getBillingInvoicesByDate(invoiceDate)` | List all invoices on a date |
| `getAllBillingInvoices()` | All invoices |
| `getCurrentMonthBillingSummary()` | Fetches invoices from 1st of current month to today; sums total amounts |
| `getBillingSummaryByDateRange(request)` | Validates fromDate <= toDate; uses JPQL SUM query |
| `deleteBillingInvoice(invoiceId)` | Delete by ID |
| `generateInvoiceNumber(date)` | Constructs `INV-YYYYMMDD-XXXX`; collision-resistant uniqueness loop |

---

### 10.7 DepartmentService / DepartmentServiceImpl

| Method | Logic |
|--------|-------|
| `addDepartment(request)` | Checks for duplicate name; creates and saves |
| `getAllDepartments()` | Returns all mapped to `DepartmentResponse` |
| `getDepartmentById(id)` | Fetch or error |
| `updateDepartment(id, request)` | Update name |
| `deleteDepartment(id)` | Delete |
| `getTotalDepartments()` | Returns `{totalDepartments: N}` map |

---

### 10.8 DoctorAvailabilityService / DoctorAvailabilityServiceImpl

| Method | Logic |
|--------|-------|
| `addAvailability(request)` | Creates `DoctorAvailability` record |
| `getAvailabilityByDoctor(doctorId)` | List all slots |
| `updateAvailability(id, request)` | Update date/time/available |
| `deleteAvailability(id)` | Delete record |
| `countAvailableDoctorsOn(date)` | Complex multi-source computation |

**countAvailableDoctorsOn(date) Algorithm:**
```text
Step 1: Get doctorIds from DoctorAvailability with specific date (available=true)
Step 2: Get doctorIds with MONTHLY_BY_DAY recurring for dayOfMonth
Step 3: Get doctorIds with WEEKLY_BY_DAY recurring for dayOfWeek
Step 4: Get doctorIds with DAILY recurring (within active date range)
Step 5: Union all into allScheduledDoctorIds (HashSet, de-duplicated)
Step 6: Get doctorIds on APPROVED leave on the date
Step 7: availableDoctorIds = allScheduled - onLeave (Set subtraction)
Step 8: Return AvailableDoctorsCountResponse with counts
```

---

### 10.9 DoctorLeaveService / DoctorLeaveServiceImpl

| Method | Logic |
|--------|-------|
| `applyLeave(request)` | Creates `DoctorLeave` with default status `PENDING` |
| `getLeaveById(id)` | Fetch |
| `getAllLeaves()` | All leave records |
| `getLeavesByDoctor(doctorId)` | Doctor-filtered |
| `updateLeave(id, request)` | Update dates/type/reason |
| `deleteLeave(id)` | Delete |
| `updateLeaveStatus(id, request)` | Set status field (APPROVED / REJECTED) |
| `countDoctorsOnLeave(date)` | Uses `countDistinctDoctorsOnLeaveByStatusAndDate(APPROVED, date)` |
| `countTotalLeaves()` | Total records |
| `countLeavesByStatus(status)` | Filter by status |
| `countActiveLeaves()` | Leaves where `CURRENT_DATE BETWEEN fromDate AND toDate` |

---

### 10.10 EmailService / EmailServiceImpl

| Method | Logic |
|--------|-------|
| `sendOtpEmail(toEmail, otp)` | Constructs `SimpleMailMessage`; sends via `JavaMailSender`; logs OTP to SLF4J at INFO level; throws `BadRequestException` on SMTP failure |

**Email Subject:** "MED.CO Password Reset OTP"
**OTP Validity Message:** "This OTP is valid for 5 minutes."

---

### 10.11 MasterService / MasterServiceImpl

| Method | Logic |
|--------|-------|
| `createMasterRecord(appointment)` | Builds `Master` entity from Appointment and saves |
| `getAllMasters()` | Returns all master records |
| `getMasterById(id)` | Fetch by ID |
| `getByMrn(mrnNo)` | List by MRN |
| `deleteMaster(id)` | Delete |

---

### 10.12 CaptchaService / CaptchaServiceImpl

| Method | Logic |
|--------|-------|
| `generateCaptcha()` | 1. Generates 5-char text via `CaptchaGenerator.generateCaptchaText(5)`; 2. Renders Base64 image via `CaptchaGenerator.generateCaptchaImage(text)`; 3. Stores `{captchaId -> CaptchaCache(text, expiry)}` in `ConcurrentHashMap`; 4. Returns `CaptchaResponse(captchaId, captchaImage)` |
| `validateCaptcha(captchaId, userCaptcha)` | 1. Fetches from store; 2. Checks expiry (5 min); 3. Case-insensitive comparison; 4. Removes from store after one use |

**Storage:** In-memory `ConcurrentHashMap<String, CaptchaCache>` (resets on application restart).
**Expiry:** 5 minutes.
**Case:** Case-insensitive (`equalsIgnoreCase`).

---

### 10.13 LeaveSchedulerService / LeaveSchedulerServiceImpl

| Method | Logic |
|--------|-------|
| `updateExpiredLeavesStatus()` | Annotated `@Scheduled(cron = "0 0 6 * * *")` — runs at 6:00 AM every day; finds all `DoctorLeave` with status=APPROVED AND toDate in the past; sets status to COMPLETED; saves in batch |

---

## 11. Repository Layer

All repositories extend `JpaRepository<Entity, Long>`.

### 11.1 UserRepository
```java
Optional<UserRole> findByEmail(String email);
boolean existsByEmail(String email);
```

### 11.2 RoleRepository
```java
Optional<Role> findByRoleName(RoleType roleName);
```

### 11.3 DoctorRepository
```java
Optional<Doctor> findByUserrole(UserRole userrole);
boolean existsByEmail(String email);
boolean existsByEmailAndIdNot(String email, Long id);
boolean existsByMobileNumber(String mobileNumber);
boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);
boolean existsByMedicalRegistrationNumber(String mrn);
boolean existsByMedicalRegistrationNumberAndIdNot(String mrn, Long id);
List<Doctor> findByDepartmentDepartmentId(Long departmentId);
```

### 11.4 PatientRepository
```java
Optional<Patient> findByMrnNo(String mrnNo);
boolean existsByMrnNo(String mrnNo);
Optional<Patient> findByUserrole(UserRole userrole);
// Custom JPQL:
@Query("SELECT COUNT(DISTINCT a.patient.patientId) FROM Appointment a \n" +
       "WHERE LOWER(a.doctor.department.departmentName) = LOWER(:departmentName)")
long countPatientsByDepartmentName(@Param("departmentName") String departmentName);
```

### 11.5 AppointmentRepository
```java
List<Appointment> findByDoctor(Doctor doctor);
List<Appointment> findByPatient(Patient patient);
List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
List<Appointment> findByPatientAndAppointmentDate(Patient patient, LocalDate date);
Optional<Appointment> findByDoctorAndAppointmentDateAndAppointmentTime(Doctor doctor, LocalDate date, LocalTime time);
boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(Doctor doctor, LocalDate date, LocalTime time, AppointmentStatus status);
long countByAppointmentDate(LocalDate date);
List<Appointment> findByStatus(AppointmentStatus status);
List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);
List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);
```

### 11.6 PrescriptionRepository
```java
Optional<Prescription> findByAppointment(Appointment appointment);
boolean existsByAppointment(Appointment appointment);
List<Prescription> findByPatientPatientId(Long patientId);
List<Prescription> findByDoctorId(Long doctorId);
Optional<Prescription> findByPatientMrnNo(String mrnNo);
```

### 11.7 BillingInvoiceRepository
```java
Optional<BillingInvoice> findByInvoiceNumber(String invoiceNumber);
List<BillingInvoice> findByPatientId(Long patientId);
List<BillingInvoice> findByPatientName(String patientName);
List<BillingInvoice> findByInvoiceDate(LocalDate invoiceDate);
List<BillingInvoice> findByInvoiceDateBetween(LocalDate start, LocalDate end);
boolean existsByInvoiceNumber(String invoiceNumber);

// Custom JPQL:
@Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM BillingInvoice b \n" +
       "WHERE b.invoiceDate BETWEEN :fromDate AND :toDate")
BigDecimal calculateTotalAmountBetweenDates(LocalDate fromDate, LocalDate toDate);
```

### 11.8 DoctorAvailabilityRepository
```java
List<DoctorAvailability> findByDoctorId(Long doctorId);
Optional<DoctorAvailability> findByDoctorIdAndAvailableDateAndAvailableTrue(Long doctorId, LocalDate date);
List<DoctorAvailability> findByAvailableDateAndAvailableTrue(LocalDate date);

// Custom JPQL:
@Query("SELECT DISTINCT da.doctor.id FROM DoctorAvailability da WHERE da.availableDate = :date AND da.available = true")
List<Long> findDistinctDoctorIdsAvailableOn(LocalDate date);
```

### 11.9 DoctorRecurringAvailabilityRepository

Contains six custom JPQL queries:
- `findMonthlyByDay(dayOfMonth, date)` — MONTHLY_BY_DAY pattern match (active, within date range)
- `findWeeklyByDay(dayOfWeek, date)` — WEEKLY_BY_DAY pattern match
- `findDailyRecurrences(date)` — DAILY pattern match
- `findByDoctorId(doctorId)` — All recurring slots for a doctor
- `findDoctorIdsMonthlyByDay(dayOfMonth, date)` — Distinct doctor IDs (monthly)
- `findDoctorIdsWeeklyByDay(dayOfWeek, date)` — Distinct doctor IDs (weekly)
- `findDoctorIdsDailyRecurrences(date)` — Distinct doctor IDs (daily)

### 11.10 DoctorLeaveRepository
```java
List<DoctorLeave> findByDoctorId(Long doctorId);

@Query("SELECT COUNT(DISTINCT dl.doctor.id) FROM DoctorLeave dl \n" +
       "WHERE dl.status = :status AND :date BETWEEN dl.fromDate AND dl.toDate")
long countDistinctDoctorsOnLeaveByStatusAndDate(LeaveStatus status, LocalDate date);

@Query("SELECT dl FROM DoctorLeave dl WHERE dl.status = :status AND dl.toDate < CURRENT_DATE")
List<DoctorLeave> findExpiredLeaves(LeaveStatus status);

@Query("SELECT COUNT(dl) FROM DoctorLeave dl")
long countTotalLeaves();

@Query("SELECT COUNT(dl) FROM DoctorLeave dl WHERE dl.status = :status")
long countByStatus(LeaveStatus status);

@Query("SELECT COUNT(dl) FROM DoctorLeave dl WHERE dl.status = :status \n" +
       "AND CURRENT_DATE BETWEEN dl.fromDate AND dl.toDate")
long countActiveLeaves(LeaveStatus status);

@Query("SELECT DISTINCT dl.doctor.id FROM DoctorLeave dl \n" +
       "WHERE dl.status = 'APPROVED' AND :date BETWEEN dl.fromDate AND dl.toDate")
List<Long> findDoctorIdsOnLeave(LocalDate date);
```

### 11.11 PasswordResetOtpRepository
```java
Optional<PasswordResetOtp> findTopByEmailOrderByCreatedAtDesc(String email);
@Modifying @Transactional
void deleteByEmail(String email);
```

### 11.12 RevokedTokenRepository
```java
Optional<RevokedToken> findByToken(String token);
boolean existsByToken(String token);
```

### 11.13 MasterRepository
```java
List<Master> findByMrnNo(String mrnNo);
```

---

## 12. Security Architecture

### 12.1 SecurityConfig.java

**Configuration class** annotated with `@Configuration`, `@EnableWebSecurity`, `@EnableMethodSecurity`.

#### URL Access Rules

| URL Pattern | Access |
|------------|--------|
| `/api/auth/**` | **Permit All** (no authentication required) |
| `/swagger-ui/**` | **Permit All** |
| `/v3/api-docs/**` | **Permit All** |
| All other URLs | **Authenticated** (valid JWT required) |

#### Key Configuration Points

- **Session Management:** STATELESS — no server-side sessions
- **CSRF:** Disabled (stateless JWT architecture)
- **Password Encoder:** `BCryptPasswordEncoder`
- **Authentication Provider:** `DaoAuthenticationProvider` with `CustomUserDetailsService`

### 12.2 JwtAuthFilter.java

`OncePerRequestFilter` implementation.

**Filter Execution Logic:**
```text
1. Extract Authorization header
2. If header starts with "Bearer ":
   a. Extract token substring
   b. Check RevokedTokenRepository.existsByToken(token)
      -> If revoked: clear SecurityContext, return 401
   c. Extract username (email) via JwtUtils.extractUsername(token)
   d. If username not null AND SecurityContext has no auth:
      -> Load UserDetails via CustomUserDetailsService
      -> Validate token via JwtUtils.validateToken(token, userDetails)
      -> If valid: set UsernamePasswordAuthenticationToken in SecurityContext
3. Continue filter chain
```

### 12.3 JwtUtils.java

Utility class for JWT operations using JJWT 0.12.7.

| Method | Description |
|--------|-------------|
| `generateToken(UserDetails)` | Creates JWT signed with HMAC-SHA256; includes sub (email), iat, exp (24h) |
| `extractUsername(token)` | Extracts sub claim |
| `extractExpiration(token)` | Extracts exp claim |
| `validateToken(token, UserDetails)` | Checks username match + not expired |
| `isTokenExpired(token)` | `expiration.before(new Date())` |

### 12.4 CustomUserDetailsService.java

Implements `UserDetailsService`.

```java
public UserDetails loadUserByUsername(String email) {
    UserRole user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new User(
        user.getEmail(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()))
    );
}
```

**Authority Format:** `ROLE_ADMIN`, `ROLE_DOCTOR`, `ROLE_PATIENT`

---

## 13. Authentication Flow (JWT)

### Login Flow

```text
Client -> POST /api/auth/login
         {email, password (AES-encrypted), captchaId, captchaText}
           |
           v
       CaptchaService.validateCaptcha(captchaId, captchaText)
           | invalid -> 400 Bad Request
           | valid
           v
       EncryptionUtil.decrypt(password)
           |
           v
       CustomUserDetailsService.loadUserByUsername(email)
           | not found -> 404
           | found
           v
       BCryptPasswordEncoder.matches(plainPassword, hashedPassword)
           | mismatch -> 400 Bad Request
           | match
           v
       JwtUtils.generateToken(userDetails)
           |
           v
       Response: { token, email, role }
```

### Request Authentication Flow

```text
Client -> Request with "Authorization: Bearer <token>"
           |
           v
       JwtAuthFilter
           |
           +-- RevokedTokenRepository.existsByToken(token)
           |       true -> 401 Unauthorized
           |       false
           +-- JwtUtils.extractUsername(token)
           |
           +-- CustomUserDetailsService.loadUserByUsername(email)
           |
           +-- JwtUtils.validateToken(token, userDetails)
           |       invalid -> 403 Forbidden
           |       valid
           v
       Set SecurityContextHolder authentication
                   |
                   v
               Controller handles request
```

### Logout Flow

```text
Client -> POST /api/auth/logout  [with Bearer token]
           |
           v
       Extract token from Authorization header
           |
           v
       Save token to revoked_token table
           |
           v
       200 OK: "Logged out successfully"

Next request with same token:
       JwtAuthFilter -> existsByToken -> true -> 401
```

---

## 14. Forgot Password / OTP / Reset Password Flow

```text
Step 1: Client -> POST /api/auth/forgot-password  { "email": "user@example.com" }
           |
           +-- UserRepository.existsByEmail(email) -> false -> 404
           |                                       -> true
           +-- PasswordResetOtpRepository.deleteByEmail(email)  [cleanup old OTPs]
           |
           +-- Generate 6-digit random OTP
           |
           +-- Save PasswordResetOtp { email, otp, createdAt=now, expiresAt=now+5min, used=false }
           |
           v
       EmailService.sendOtpEmail(email, otp) -> SMTP -> Gmail -> User inbox

Step 2: Client -> POST /api/auth/verify-otp  { "email": ..., "otp": "123456" }
           |
           +-- Find latest OTP record by email (findTopByEmailOrderByCreatedAtDesc)
           +-- Check: not used AND expiresAt > now
           v
       Return success/failure

Step 3: Client -> POST /api/auth/reset-password  { "email": ..., "otp": ..., "newPassword": "..." }
           |
           +-- Re-verify OTP (same checks as Step 2)
           +-- Load UserRole by email
           +-- BCryptPasswordEncoder.encode(newPassword)
           +-- UserRole.setPassword(encoded)
           +-- userRepository.save(user)
           v
       Mark OTP as used (otp.setUsed(true)); save
```

---

## 15. CAPTCHA Sub-System

### Components

| Component | Description |
|-----------|-------------|
| `CaptchaGenerator` | Static utility: generates 5-char alphanumeric text; renders Kaptcha image; returns Base64 string |
| `CaptchaCache` | POJO: `captcha` (String), `expiryTime` (LocalDateTime) |
| `CaptchaServiceImpl` | Manages `ConcurrentHashMap<String, CaptchaCache>`; generate + validate |
| `CaptchaResponse` | DTO: `captchaId` (UUID), `captchaImage` (Base64 JPEG) |

### Flow

```text
Client -> GET /api/auth/create-captcha
          Response: { captchaId: "uuid", captchaImage: "data:image/jpeg;base64,..." }
          
Client -> POST /api/auth/login { captchaId: "uuid", captchaText: "AB3XY", ... }
          AuthServiceImpl validates: CaptchaService.validateCaptcha(captchaId, captchaText)
          -> Removes from store after one use (single-use CAPTCHA)
```

---

## 16. Exception Handling

### Custom Exceptions

| Exception Class | HTTP Status | Use Case |
|----------------|-------------|---------|
| `ResourceNotFoundException` | 404 Not Found | Entity not found by ID |
| `BadRequestException` | 400 Bad Request | Business rule violation |
| `ResourceAlreadyExistsException` | 409 Conflict | Duplicate data |

### GlobalExceptionHandler.java

`@RestControllerAdvice` — intercepts all exceptions.

| Handler | Exception | HTTP Status | Response |
|---------|-----------|-------------|---------|
| `handleResourceNotFoundException` | `ResourceNotFoundException` | 404 | `ApiResponse(404, message, null)` |
| `handleBadRequestException` | `BadRequestException` | 400 | `ApiResponse(400, message, null)` |
| `handleResourceAlreadyExistsException` | `ResourceAlreadyExistsException` | 409 | `ApiResponse(409, message, null)` |
| `handleValidationException` | `MethodArgumentNotValidException` | 400 | `ApiResponse(400, "Validation Failed", {field: error})` |
| `handleConstraintViolationException` | `ConstraintViolationException` | 400 | `ApiResponse(400, "Validation Failed", {field: error})` |
| `handleException` | `Exception` (catch-all) | 500 | `ApiResponse(500, "Something went wrong", ex.getMessage())` |

### Standard API Response Structure

```json
{
  "status": 200,
  "message": "Success message",
  "data": { "..." : "..." }
}
```

---

## 17. Utility Classes

### 17.1 EncryptionUtil.java

**Package:** `com.med.co.util`

Provides AES/CBC/PKCS5Padding symmetric encryption and decryption.

| Field | Value |
|-------|-------|
| `SECRET_KEY` | `"HealthBridgeSecretKeyForAuth1234"` (32 bytes) |
| `INIT_VECTOR` | `"1234567890123456"` (16 bytes) |
| Algorithm | `AES/CBC/PKCS5Padding` |

**Methods:**
```java
public static String encrypt(String plainText)     // -> Base64 encoded cipher text
public static String decrypt(String encryptedText) // -> plaintext; fallback returns input on error
```

**Usage:** `AuthServiceImpl.login()` calls `EncryptionUtil.decrypt(request.getPassword())` — the frontend is expected to send the password AES-encrypted.

---

### 17.2 PrescriptionHtmlGenerator.java

**Package:** `com.med.co.util`

Static utility that generates a fully-styled HTML prescription document by filling a template.

**Method:**
```java
public static String generatePrescriptionHtml(Appointment appointment, PrescriptionRequestDto requestDto)
```

**Template Tokens Replaced:**

| Token | Value Source |
|-------|-------------|
| `{{DOCTOR_NAME}}` | `doctor.firstName + " " + doctor.lastName` |
| `{{SPECIALIZATION}}` | `doctor.specialization` |
| `{{HOSPITAL_NAME}}` | Hardcoded: "Health Bridge" |
| `{{HOSPITAL_ADDRESS}}` | Hardcoded: "Bhubaneswar, Odisha" |
| `{{PATIENT_NAME}}` | `patient.firstName + " " + patient.lastName` |
| `{{AGE}}` | Computed: `Period.between(patient.dateOfBirth, LocalDate.now()).getYears()` |
| `{{DATE}}` | `appointment.appointmentDate.toString()` |
| `{{MRN_NO}}` | `patient.mrnNo` |
| `{{ADDRESS}}` | `patient.address` |
| `{{DIAGNOSIS}}` | `requestDto.diagnosis` |
| `{{MEDICINES}}` | `requestDto.medicines` |
| `{{ADVICE}}` | `requestDto.advice` |

The generated HTML is stored in `prescription.prescriptionHtml` (`@Lob` field).

---

## 18. Scheduled Tasks

### LeaveSchedulerServiceImpl

| Property | Value |
|----------|-------|
| Cron Expression | `0 0 6 * * *` |
| Fires At | Every day at **6:00 AM** |
| Purpose | Updates APPROVED doctor leaves whose toDate is in the past to COMPLETED |
| Annotation | `@Scheduled(cron = "0 0 6 * * *")` on `updateExpiredLeavesStatus()` |
| Error Handling | try-catch — logs error, does not crash the application |

Enabling Scheduling: `@EnableScheduling` must be present on `MedCoApplication.java` or a `@Configuration` class.

---

## 19. OpenAPI / Swagger Documentation

**Dependency:** `springdoc-openapi-starter-webmvc-ui` v2.8.8

**Configuration:** `OpenApiConfig.java` (in `config/` package)

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("MED.CO API")
            .description("Hospital Management System API")
            .version("1.0"));
}
```

**Access URLs (development):**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Both URLs are **permitted without authentication** in `SecurityConfig`.

---

## 20. Email (SMTP) Configuration

**Dependency:** `spring-boot-starter-mail`

**Configuration:** `MailConfig.java` (in `config/` package)

| Property | Value |
|---------|-------|
| SMTP Host | `smtp.gmail.com` |
| Port | `587` |
| Transport Security | STARTTLS |
| Auth | Gmail App Password |
| From Address | Configured via `spring.mail.username` |

**Email Template (OTP Email):**
```text
Subject: MED.CO Password Reset OTP

Dear User,

Your OTP for resetting your MED.CO account password is: <OTP>

This OTP is valid for 5 minutes.

Please do not share this OTP with anyone.

Regards,
MED.CO Team
```

**Logging:** OTP is also logged to SLF4J at INFO level:
```text
=================================================
 [MED.CO OTP GENERATED] Target: user@email.com | OTP: 123456
=================================================
```

---

## 21. Enumerations

### Enums.java (nested static enums)

| Enum | Values |
|------|--------|
| `RoleType` | `ADMIN`, `DOCTOR`, `PATIENT` |
| `DoctorStatus` | `AVAILABLE`, `UNAVAILABLE`, `ON_LEAVE` |
| `Gender` | `MALE`, `FEMALE`, `OTHER` |
| `BloodGroup` | `A_POSITIVE`, `A_NEGATIVE`, `B_POSITIVE`, `B_NEGATIVE`, `AB_POSITIVE`, `AB_NEGATIVE`, `O_POSITIVE`, `O_NEGATIVE` |

### AppointmentStatus.java

| Value | Meaning |
|-------|---------|
| `SCHEDULED` | Appointment is upcoming |
| `COMPLETED` | Appointment has happened |
| `CANCELLED` | Appointment was cancelled |

### LeaveStatus.java

| Value | Meaning |
|-------|---------|
| `PENDING` | Leave applied, awaiting decision |
| `APPROVED` | Leave approved |
| `REJECTED` | Leave rejected |
| `COMPLETED` | Leave period has passed (set by scheduler) |

### DoctorRecurringAvailability.RecurrenceType (inner enum)

| Value | Meaning |
|-------|---------|
| `MONTHLY_BY_DAY` | Available on specific day(s) of each month |
| `WEEKLY_BY_DAY` | Available on specific day(s) of each week |
| `DAILY` | Available every day (optionally within date range) |

---

## 22. Database Migration SQL

**File:** `database_migration.sql` (in project root)

This script handles creation of `doctor_recurring_availability` table and related indexes. It is NOT managed by Flyway or Liquibase — it is a manual SQL script.

### Key DDL

```sql
CREATE TABLE doctor_recurring_availability (
    recurring_availability_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    recurrence_type VARCHAR(50) NOT NULL DEFAULT 'MONTHLY_BY_DAY',
    day_of_month INT,
    day_of_week INT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    start_date DATE,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE
);
```

### Performance Indexes

```sql
CREATE INDEX idx_doctor_availability_date_available ON doctor_availability(available_date, available);
CREATE INDEX idx_doctor_leave_dates_status ON doctor_leave(from_date, to_date, status);
CREATE INDEX idx_doctor_recurring_date_range ON doctor_recurring_availability(start_date, end_date, active);
```

---

## 23. Inter-Module Data Flow Diagrams

### Appointment Booking Flow

```text
Client
  |
  v  POST /api/appointments/book { doctorId, patientId, date, time, reason }
  |
AppointmentController
  |
  v  appointmentService.bookAppointment(dto)
  |
AppointmentServiceImpl
  +---> DoctorRepository.findById()           -> Doctor entity
  +---> PatientRepository.findById()          -> Patient entity
  +---> DoctorLeaveRepository                -> check APPROVED leave on date
  +---> DoctorAvailabilityRepository         -> check available=true on date
  +---> AppointmentRepository.existsBy...()  -> check slot conflict
  +---> AppointmentRepository.save()         -> saves Appointment (status=SCHEDULED)
  +---> MasterService.createMasterRecord()
            |
            v  MasterRepository.save()        -> saves Master record
  |
  v  AppointmentResponseDto
Client <- 201 Created
```

### Invoice Creation Flow

```text
Client
  |
  v  POST /api/billing/create/invoice { patientId, patientName, invoiceDate, doctorConsultation, medicines[] }
  |
BillingInvoiceController
  |
  v  billingInvoiceService.createBillingInvoice(dto)
  |
BillingInvoiceServiceImpl
  +-- medicineTotal[] = unitPrice x quantity (per item)
  +-- medicineSubtotal = sum of all medicineTotals
  +-- doctorSubtotal = doctorFee
  +-- serviceCharge = 10.00
  +-- subtotalBeforeTax = doctor + medicine + service
  +-- tax = subtotalBeforeTax x 5%
  +-- totalAmount = doctor + medicine + service + tax
  +-- invoiceNumber = generateInvoiceNumber(date) -> "INV-YYYYMMDD-0001"
  +-- BillingInvoiceRepository.save()
  |
  v  BillingInvoiceResponseDto (full breakdown)
Client <- 201 Created
```

---

## 24. Implemented vs Not-Implemented Features

### Implemented

| Feature | Module |
|---------|--------|
| JWT Authentication (Login) | `AuthService`, `JwtUtils`, `JwtAuthFilter` |
| JWT Logout (token blacklisting) | `RevokedToken`, `AuthService` |
| Forgot Password via OTP email | `AuthService`, `EmailService`, `PasswordResetOtp` |
| OTP Verification | `AuthService` |
| Reset Password with OTP | `AuthService` |
| CAPTCHA generation + validation | `CaptchaServiceImpl`, `CaptchaGenerator` |
| AES password decryption on login | `EncryptionUtil` |
| BCrypt password hashing | `PasswordEncoder` bean |
| Doctor CRUD | `DoctorServiceImpl` |
| Patient CRUD + auto MRN | `PatientServiceImpl` |
| Department CRUD | `DepartmentServiceImpl` |
| Appointment booking with conflict checks | `AppointmentServiceImpl` |
| Appointment cancellation | `AppointmentServiceImpl` |
| Available dates / time slots query | `AppointmentServiceImpl` |
| Prescription creation + HTML generation | `PrescriptionServiceImpl`, `PrescriptionHtmlGenerator` |
| Prescription retrieval (multiple ways) | `PrescriptionServiceImpl` |
| Billing invoice creation with tax | `BillingInvoiceServiceImpl` |
| Monthly billing summary | `BillingInvoiceServiceImpl` |
| Date-range billing summary | `BillingInvoiceServiceImpl` |
| Doctor specific-date availability | `DoctorAvailabilityServiceImpl` |
| Doctor recurring availability (3 patterns) | `DoctorAvailabilityServiceImpl`, `DoctorRecurringAvailabilityRepository` |
| Available doctors count for a date | `DoctorAvailabilityServiceImpl` |
| Doctor leave apply / update / delete | `DoctorLeaveServiceImpl` |
| Leave status update (approve/reject) | `DoctorLeaveServiceImpl` |
| Leave count queries | `DoctorLeaveServiceImpl` |
| Automatic leave completion (scheduler) | `LeaveSchedulerServiceImpl` |
| Master record auto-creation on booking | `MasterServiceImpl` + `AppointmentServiceImpl` |
| Patients count by department | `PatientServiceImpl` |
| Role-based access setup | `SecurityConfig`, `UserRole`, `Role` |
| Global exception handling | `GlobalExceptionHandler` |
| Bean validation | `@Valid`, Jakarta Bean Validation |
| Swagger / OpenAPI docs | `OpenApiConfig`, SpringDoc |
| SMTP email (OTP mail) | `EmailServiceImpl`, `MailConfig` |
| Pagination + sorting (Doctors, Patients) | `DoctorServiceImpl`, `PatientServiceImpl` |

### Not Implemented

| Feature | Notes |
|---------|-------|
| `DoctorService.updateLeaveStatus()` (in DoctorController) | Method body exists but returns `null` with a `TODO` comment |
| Liquibase / Flyway migrations | No migration tool; using `ddl-auto=update` |
| `application.yml` | Not present; only `application.properties` |
| PDF generation from prescription | `openpdf` dependency declared but no PdfGenerator class found |
| Fine-grained role-level URL restrictions | `SecurityConfig` only enforces authenticated access; no role-specific URL rules configured |
| Refresh Token mechanism | No refresh token endpoint or entity |
| Token expiry / revoked_token cleanup | `RevokedToken` table grows indefinitely; no cleanup job |
| File upload / profile picture | Not implemented |
| Appointment reminder notifications | Not implemented |
| Doctor linked to UserRole directly | `Doctor` entity has no `@OneToOne UserRole` field (unlike `Patient`) |

---

*End of MED.CO Backend Knowledge Base*

---

Document generated from complete source code inspection of `c:\Users\Lenovo\Desktop\Project\MED.CO`
Last verified against: all `.java` files in `src/main/java/com/med/co/`, `pom.xml`, `application.properties`, `database_migration.sql`
