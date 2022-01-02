plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.example.nearbyfoursquare"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")

    // Compose
    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.compose.material:material:1.0.5")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-rc01")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0-rc03")
    implementation("androidx.compose.ui:ui-tooling:1.0.5")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")

    // Paging
    implementation("androidx.paging:paging-runtime:3.1.0")
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")
    testImplementation("androidx.paging:paging-common:3.1.0")

    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.37")
    kapt("com.google.dagger:hilt-android-compiler:2.37")
}