# BMI Calculator Pro

A Kotlin Android application for calculating Body Mass Index (BMI), classifying the result, storing the latest measurements, and presenting diet-oriented content through a Jetpack Compose interface.

> This repository is developed with AI-assisted engineering. Product decisions, verification, review, and published changes remain human-directed.

## Current status

- Kotlin + Jetpack Compose Android application
- Compose home screen connected to the tested BMI domain pipeline
- Metric and imperial BMI calculation domain model
- Locale-tolerant metric form validation for comma and dot decimal separators
- Field-specific Turkish errors for malformed and out-of-range values
- Salted PBKDF2 verification for local profile passwords
- JVM unit tests for formulas, category boundaries, parsing, validation, and password verification
- Automatic GitHub Actions verification on every push and pull request
- Debug APK generated as a workflow artifact after successful pushes

## Features

- Calculate BMI from height and weight
- Classify results as underweight, normal, overweight, or obese
- Support metric and imperial calculations in the domain layer
- Accept Turkish-style decimal input such as `75,5`
- Reject blank, non-numeric, non-finite, and implausible values before calculation
- Store the latest height, weight, BMI, ideal weight, and timestamp locally
- Protect local profile passwords with a random salt and PBKDF2-derived verifier
- Display previous measurement information
- Navigate between home, history, diet lists, and profile screens
- Schedule diet reminder notifications

## Architecture

The current app is being incrementally separated into testable layers:

```text
app/src/main/java/com/enesakin/vkhesaplama/
├── MainActivity.kt                  # Compose navigation, local preferences, notifications
├── Home.kt                          # BMI form state and presentation
├── domain/
│   ├── BmiCalculator.kt             # Pure BMI formula, range validation, result and category model
│   └── BmiInputEvaluator.kt         # Text parsing and typed validation result model
├── security/
│   └── PasswordVerifier.kt          # Salted PBKDF2 password derivation and constant-time verification
└── ui/theme/                        # Compose theme definitions

app/src/test/java/com/enesakin/vkhesaplama/
├── domain/
│   ├── BmiCalculatorTest.kt         # Formula and category boundary tests
│   └── BmiInputEvaluatorTest.kt     # Localized input and validation tests
└── security/
    └── PasswordVerifierTest.kt       # Hashing, random salt, malformed data, and verification tests
```

The domain and security packages are independent from Android and Compose. The UI delegates calculation, validation, and password verification instead of duplicating these rules inside composables.

### BMI input flow

```text
Compose text fields
        ↓
BmiInputEvaluator
        ↓
BmiCalculator
        ↓
BmiResult or BmiInputError
        ↓
Result UI or Turkish validation message
```

`BmiInputEvaluator` normalizes comma decimals, trims whitespace, rejects `NaN` and infinity, and returns field-specific errors instead of throwing from the UI layer.

### Local profile password flow

```text
Password entered during registration
        ↓
Random 16-byte salt + PBKDF2-HMAC-SHA1 (120,000 iterations)
        ↓
Versioned verifier stored in SharedPreferences
        ↓
Constant-time verification during login
```

Plain-text passwords are no longer written to preferences or application logs. Existing legacy profiles containing a plain-text value are deliberately rejected and must be recreated from the registration screen. This is a local profile gate, not server-backed authentication.

## Technology stack

- Kotlin
- Jetpack Compose and Material 3
- Android Navigation Compose
- AndroidX WorkManager
- JUnit 4
- Gradle 8.6
- Android Gradle Plugin 8.4
- GitHub Actions

## Build and run

### Requirements

- JDK 17
- Android Studio with Android SDK 34

### Local verification

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

The generated debug APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

### Android Studio

1. Clone this repository.
2. Open the project root in Android Studio.
3. Allow Gradle sync to complete.
4. Select an emulator or Android device running API 25 or newer.
5. Run the `app` configuration.

## Continuous integration

`.github/workflows/android-ci.yml` runs automatically for pushes and pull requests targeting `main`.

The pipeline:

1. Configures Temurin JDK 17.
2. Restores and caches Gradle dependencies.
3. Runs JVM unit tests.
4. Runs Android lint.
5. Builds a debug APK.
6. Uploads the APK as a 14-day workflow artifact for successful pushes.

## Screenshots

| Home | History | Diet lists | Profile |
|---|---|---|---|
| ![Home screen](homescreen.jpeg) | ![History screen](pastscreen.jpeg) | ![Diet lists](dietlists.jpeg) | ![Profile screen](profilescreen.jpeg) |

## Calculation behavior

Metric formula:

```text
BMI = weightKg / heightMeters²
```

Imperial formula:

```text
BMI = 703 × weightLb / heightInches²
```

Metric form values are accepted only when weight is between 20–500 kg and height is between 80–250 cm. Results are rounded to one decimal place.

## Roadmap

- Remove the obsolete duplicate `calculateBMI` UI helper
- Replace the large activity file with screen, state, and data packages
- Move local state to DataStore
- Replace the local profile gate with server-backed authentication if real accounts are required
- Implement Android 13+ notification permission correctly
- Add Compose UI tests and accessibility checks
- Produce signed release builds through a protected release workflow

## Privacy and health disclaimer

BMI is a general screening metric and is not a medical diagnosis. The application should not replace guidance from a qualified healthcare professional.

Profile and measurement values are stored locally on the device. Passwords are represented only by a salted PBKDF2 verifier, but the profile feature is not a substitute for a real authenticated account system.

## License

Licensed under the MIT License. See [LICENSE](LICENSE).

## Contact

Enes Akın — [GitHub profile](https://github.com/enesakn16)
