# Swift4j Toolkit Examples

The repository contains ready to build usage examples for the [Swift4j Toolkit](https://github.com/scade-platform/swift4j.git).

## Structure

Each example is self-contained in its own directory:

```
<ExampleName>/
├── Packages/<ExampleName>/   # Swift source
├── Android/                  # Android app
└── Kotlin/                   # Kotlin app (where applicable)
```

### Examples

| Directory | Description |
|-----------|-------------|
| `SalesforceDemo` | Fetch Salesforce accounts using OAuth2 client credentials — see [SalesforceDemo/README.md](SalesforceDemo/README.md) for configuration |
| `WeatherService` | Weather data service |
| `SimpleDB` | SQLite database access via GRDB |
| `ObservationDemo` | Swift Observation framework |
| `swift4j-examples` | General swift4j usage examples |

## Prerequisites

- [Swift4j Prerequisites](https://github.com/scade-platform/swift4j.git#Prerequisites)
- Gradle >= 8.5
- Android Studio Ladybug or later

## Building

### Android

Build from the command line:

```bash
cd <ExampleName>/Android
gradle assembleDebug
```

Or open the `<ExampleName>/Android` directory in Android Studio.

### Kotlin

```bash
cd <ExampleName>/Kotlin
./gradlew run
```
