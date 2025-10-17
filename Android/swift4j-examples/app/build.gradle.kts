import io.scade.gradle.plugins.spm.TargetPlatform

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("io.scade.gradle.plugins.android.swiftpm") version "1.4.2"
}

android {
    namespace = "com.example.swiftexamples"
    compileSdk = 35

    // If you want to use a specific NDK version, uncomment the line below and specify the version.
    //ndkVersion = "29.0.14206865"

    defaultConfig {
        applicationId = "com.example.swiftexamples"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

swiftpm {
    path = file("../../../Packages/swift4j-examples")
    product = "swift4j-examples"
    javaVersion = 8
    scdAutoUpdate = true

    // If you want to only build for specific architectures, uncomment the line below.
    // By default, arm64-v8a and x86_64 architectures are built.
    //platforms = listOf(TargetPlatform.Android(archs = listOf("arm64-v8a")))
}