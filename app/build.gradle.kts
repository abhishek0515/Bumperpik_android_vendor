import com.android.build.gradle.internal.api.ApkVariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.bumperpick.bumperpickvendor"
    compileSdk = 35
    applicationVariants.all {
        if (buildType.name == "debug") {
            outputs.all {
                // Cast to ApkVariantOutputImpl to access outputFileName
                val outputImpl = this as ApkVariantOutputImpl
                val appName = "BumperPick Vendor" // Change as needed
                outputImpl.outputFileName = "$appName-${name}.apk"
            }
        }
    }
    defaultConfig {
        applicationId = "com.bumperpick.bumperpickvendor"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation ("com.auth0:java-jwt:4.4.0")

    implementation ("androidx.credentials:credentials:1.2.2")
    implementation ("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation( "com.google.android.libraries.identity.googleid:googleid:1.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    // Credential Manager dependencies
    implementation("androidx.credentials:credentials:1.3.0")

    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
// DataStore for SharedPreferences
    implementation("androidx.datastore:datastore-preferences:1.1.6")

// ViewModel
    implementation("io.coil-kt:coil-compose:2.5.0")
// Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("com.google.accompanist:accompanist-pager:0.32.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.32.0")

    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation (libs.koin.androidx.compose)
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