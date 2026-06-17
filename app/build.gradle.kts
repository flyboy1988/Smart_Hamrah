plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)          // ✅ Required for Kotlin
    alias(libs.plugins.kotlin.compose)          // ✅ Compose compiler plugin
    alias(libs.plugins.ksp)                     // ✅ For Room + Hilt (KSP)
    alias(libs.plugins.hilt)                    // ✅ Dagger Hilt
}

android {
    namespace = "com.smartbank.smarthamrah"
    compileSdk = 36                              // ✅ Fixed: was invalid syntax

    defaultConfig {
        applicationId = "com.smartbank.smarthamrah"
        minSdk = 24
        targetSdk = 36
        versionCode= 1
        versionName= "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Compatible with Kotlin 2.0.21 and Compose BOM 2024.10.00
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.appcompat)

    // Persian Date
    implementation(libs.persiandate)
    implementation(libs.persianrangedatepicker)
    implementation(libs.ktoasty)

    // Retrofit + Network
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Barcode reader
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        isTransitive = false
    }
    implementation(libs.core)
    implementation(libs.timber)

    // Local JARs (if any)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // AndroidX + Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.coil.compose)
    // Lottie animations
    implementation(libs.coil.compose)
    implementation(libs.androidx.datastore.preferences)
}