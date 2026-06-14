# TOP UP E-WALLET – AKEYMA E-Wallet

## Overview / Application Description

AKEYMA E-Wallet is a mobile digital wallet application developed using Android Studio and Kotlin. The application provides users with a modern and user-friendly platform to manage digital financial transactions such as balance top-ups, money transfers, QRIS payments, transaction history tracking, and promotional voucher redemption.

The application integrates Firebase services for authentication, cloud database storage, analytics, and crash reporting. AKEYMA E-Wallet is designed to simulate the core features of modern digital wallet applications such as DANA, OVO, and GoPay while maintaining a clean user experience and secure transaction management.

### Main Features

* User Authentication (Firebase Authentication)
* Real-Time Balance Management
* Top-Up Balance
* Money Transfer
* QRIS Scanner Payment
* Transaction History
* Promo & Voucher System
* User Profile Management
* Search Functionality
* Firebase Analytics Integration
* Firebase Crashlytics Integration

---

## Source Data (Data Model)

### Database Platform

The application uses **Firebase Firestore** as its cloud database.

### Main Collections

#### users

Stores user account information.

| Field    | Type   | Description            |
| -------- | ------ | ---------------------- |
| uid      | String | Unique user identifier |
| username | String | User display name      |
| email    | String | User email             |
| balance  | Long   | Current wallet balance |

#### topup_transactions

Stores user transaction history.

| Field          | Type      | Description                |
| -------------- | --------- | -------------------------- |
| transaction_id | String    | Transaction identifier     |
| user_id        | String    | User reference             |
| amount         | Long      | Transaction amount         |
| payment_method | String    | Payment method used        |
| timestamp      | Timestamp | Transaction time           |
| date_string    | String    | Formatted transaction date |

### Data Flow

1. User logs in using Firebase Authentication.
2. User information is retrieved from Firestore.
3. Wallet balance is synchronized in real time using Snapshot Listeners.
4. Transactions are stored in Firestore collections.
5. Transaction history is displayed using RecyclerView.
6. Promo and voucher information are managed within the application.

---

## Pre-requisites

Before running this project, make sure the following software is installed:

### Software Requirements

* Android Studio (Latest Stable Version Recommended)
* JDK 17
* Gradle 8+
* Android SDK 34
* Git

### Firebase Requirements

Create a Firebase project and enable:

* Firebase Authentication
* Cloud Firestore
* Firebase Analytics
* Firebase Crashlytics

Download and place:

```text
google-services.json
```

inside:

```text
app/google-services.json
```

---

## Dependencies

The project uses the following dependencies:

### AndroidX

* androidx.core:core-ktx
* androidx.appcompat:appcompat
* androidx.constraintlayout:constraintlayout
* androidx.recyclerview:recyclerview
* androidx.cardview:cardview

### Material Design

* com.google.android.material:material

### Navigation Component

* androidx.navigation:navigation-fragment-ktx
* androidx.navigation:navigation-ui-ktx

### Image Processing

* Glide (com.github.bumptech.glide:glide)

### Profile Image

* CircleImageView (de.hdodenhof:circleimageview)

### QR & Barcode Scanner

* ZXing Android Embedded
* ZXing Core

### Firebase

* Firebase Analytics
* Firebase Authentication
* Firebase Firestore
* Firebase Crashlytics

---

## Plugins / Add-ins

### Android Gradle Plugin

```gradle
com.android.application
```

### Kotlin Android Plugin

```gradle
org.jetbrains.kotlin.android
```

### Google Services Plugin

```gradle
com.google.gms.google-services
```

Used to connect the application with Firebase services.

### Firebase Crashlytics Plugin

```gradle
com.google.firebase.crashlytics
```

Used for crash monitoring and application diagnostics.

### Build Features

* View Binding
* Firebase Integration
* Navigation Component
* QRIS Scanner Integration

---

## Technology Stack

| Technology              | Usage                     |
| ----------------------- | ------------------------- |
| Kotlin                  | Main Programming Language |
| Android Studio          | Development Environment   |
| Firebase Authentication | User Authentication       |
| Firebase Firestore      | Cloud Database            |
| Firebase Analytics      | User Analytics            |
| Firebase Crashlytics    | Crash Reporting           |
| Navigation Component    | App Navigation            |
| Glide                   | Image Loading             |
| ZXing                   | QRIS Scanner              |
| RecyclerView            | Dynamic Lists             |
| Material Design         | User Interface            |

---

## Project Structure

```text
com.akeyma.ewallet
│
├── activity
├── adapter
├── fragment
├── model
├── utils
├── binding
│
├── HomeFragment
├── TopUpFragment
├── TransactionFragment
├── PromoFragment
├── ProfileFragment
│
└── Firebase Integration
```

---
