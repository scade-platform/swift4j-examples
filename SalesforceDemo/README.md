# SalesforceDemo

A sample application demonstrating how to use Salesforce from Swift with SCADE. It authenticates via the OAuth2 client credentials flow and displays a list of Salesforce accounts on Android.

## Structure

```
SalesforceDemo/
├── Packages/SalesforceDemo/   # Swift source
└── Android/                   # Android app (Jetpack Compose)
```

## Configuring credentials

The app requires a Salesforce connected app with the client credentials flow enabled. You need:

- **Client ID** — the connected app's consumer key
- **Client Secret** — the connected app's consumer secret
- **Base Domain** — your org's domain (e.g. `your-org.my.salesforce.com`)

### Android app

Add the credentials to `Android/local.properties` (git-ignored):

```properties
salesforce.clientId=YOUR_CLIENT_ID
salesforce.clientSecret=YOUR_CLIENT_SECRET
salesforce.baseDomain=your-org.my.salesforce.com
```

### Swift tests (`swift test`)

Pass credentials on the command line — they are baked into the test binary at build time:

```bash
SF_CLIENT_ID=... SF_CLIENT_SECRET=... SF_BASE_DOMAIN=... swift test
```

### Android tests (`scd test`)

Pass credentials on the command line — they are baked into the test binary at build time:

```bash
SF_CLIENT_ID=... SF_CLIENT_SECRET=... SF_BASE_DOMAIN=... scd test --platform android-arm64-v8a
```

## Building

Build from the command line:

```bash
cd Android
gradle assembleDebug
```

Or open the `Android` directory in Android Studio.
