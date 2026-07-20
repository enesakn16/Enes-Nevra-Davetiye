# BMI Calculator Pro

A Kotlin Android application for calculating Body Mass Index (BMI), classifying the result, storing the latest measurements, and presenting diet-oriented content through a Jetpack Compose interface.

> This repository is developed with AI-assisted engineering. Product decisions, verification, review, and published changes remain human-directed.

## Current status

- Kotlin + Jetpack Compose Android application
- Compose home screen connected to the tested BMI domain pipeline
- Metric and imperial BMI calculation domain model
- Locale-tolerant metric form validation for comma and dot decimal separators
- Field-specific Turkish errors for malformed and out-of-range values
- JVM unit tests for formulas, category boundaries, parsing, and validation
- Automatic GitHub Actions verification on every push and pull request
- Debug APK generated as a workflow artifact after successful pushes

## Features

- Calculate BMI from height and weight
- Classify results as underweight, normal, overweight, or obese
- Support metric and imperial calculations in the domain layer
- Accept Turkish-style decimal input such as `75,5`
- Reject blank, non-numeric, non-finite, and implausible values before calculation
- Store the latest height, weight, BMI, ideal weight, and timestamp locally
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
└── ui/theme/                        # Compose theme definitions

app/src/test/java/com/enesakin/vkhesaplama/domain/
├── BmiCalculatorTest.kt             # Formula and category boundary tests
└── BmiInputEvaluatorTest.kt         # Localized input and validation tests
```

The domain package is independent from Android and Compose. The UI delegates parsing, range checks, formula calculation, and classification to this package instead of duplicating business rules inside composables.

### Input flow

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
- Remove plain-text credential storage and introduce an appropriate authentication model
- Implement Android 13+ notification permission correctly
- Add Compose UI tests and accessibility checks
- Produce signed release builds through a protected release workflow

## Privacy and health disclaimer

BMI is a general screening metric and is not a medical diagnosis. The application should not replace guidance from a qualified healthcare professional.

The current legacy profile implementation stores local form values on the device. Do not use a real password until the planned authentication and secure-storage refactor is complete.

## License

Licensed under the MIT License. See [LICENSE](LICENSE).

## Contact

Enes Akın — [GitHub profile](https://github.com/enesakn16)
