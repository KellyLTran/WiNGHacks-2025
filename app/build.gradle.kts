plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // safe args for navigation
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version "1.9.24"
}

android {
    namespace = "com.example.barcodescannerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.barcodescannerapp"
        minSdk = 28
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
        viewBinding = true
    }
}

dependencies {
    // Add Apache POI Dependencies for reading excel files
    implementation(libs.apache.poi)
    implementation(libs.apache.poi.ooxml)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.mlkit:barcode-scanning:17.3.0")
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.1.0-alpha06")

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
}