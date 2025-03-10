plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.miguel.casinoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.miguel.casinoapp"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    // Retrofit for API handling
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor.v4110)

    // Coil for image loading in Compose
    implementation(libs.coil)

    // AndroidX and Jetpack Compose
    implementation(platform(libs.androidx.compose.bom.v20241201))
    implementation(libs.androidx.activity.compose)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.firebase.auth.ktx)

    // Testing
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Debugging
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.ui.tooling)

    // Firebase
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    //Add more firebase dependencies here
    implementation(libs.firebase.analytics)
}