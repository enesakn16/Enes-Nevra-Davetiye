# BMI Calculator Pro

A Kotlin Android application for calculating Body Mass Index (BMI), classifying the result, storing the latest measurements, and presenting diet-oriented content through a Jetpack Compose interface.

> This repository is developed with AI-assisted engineering. Product decisions, verification, review, and published changes remain human-directed.

## Current status

- Kotlin + Jetpack Compose Android application
- Metric and imperial BMI calculation domain model
- Unit tests for formulas, category boundaries, and invalid input
- Automatic GitHub Actions verification on every push and pull request
- Debug APK generated as a workflow artifact after successful pushes

## Features

- Calculate BMI from height and weight
- Classify results as underweight, normal, overweight, or obese
- Support metric and imperial calculations in the domain layer
- Store the latest height, weight, BMI, ideal weight, and timestamp locally
- Display previous measurement information
- Navigate between home, history, diet lists, and profile screens
- Schedule diet reminder notifications

## Architecture

The current app is being incrementally separated into testable layers:

```text
app/src/main/java/com/enesakin/vkhesaplama/
├── MainActivity.kt              # Compose navigation, UI, local preferences, notifications
├── domain/
│   └── BmiCalculator.kt         # Pure BMI formula, validation, result and category model
└── ui/theme/                    # Compose theme definitions

app/src/test/java/com/enesakin/vkhesaplama/domain/
└── BmiCalculatorTest.kt         # JVM unit tests for the calculation domain
```

`BmiCalculator` is intentionally independent from Android and Compose. This keeps the calculation deterministic, reusable, and fast to test on the JVM.

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

Input ranges are validated before calculation to reject impossible or accidental zero values. Results are rounded to one decimal place.

## Roadmap

- Connect the Compose home screen directly to the new domain calculator
- Replace the large activity file with screen, state, and data packages
- Move local state to DataStore
- Remove plain-text credential storage and introduce an appropriate authentication model
- Add Compose UI tests and accessibility checks
- Produce signed release builds through a protected release workflow

## Privacy and health disclaimer

BMI is a general screening metric and is not a medical diagnosis. The application should not replace guidance from a qualified healthcare professional.

The current legacy profile implementation stores local form values on the device. Do not use a real password until the planned authentication and secure-storage refactor is complete.

## License

No open-source license has been selected yet. All rights are reserved until a license is added.

## Contact

Enes Akın — [GitHub profile](https://github.com/enesakn16)
