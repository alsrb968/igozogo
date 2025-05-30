plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.igozogo.android.hilt)
}

android {
    namespace = "io.jacob.igozogo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.jacob.igozogo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
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
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.design)
    implementation(projects.feature.home)
    implementation(projects.feature.bookmark)

    implementation(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    //----- Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
    debugImplementation(libs.bundles.androidx.compose.debug)

    //----- Coil
    implementation(libs.coil.compose)

    //----- Paging
    implementation(libs.androidx.paging.compose)

//    //----- Dagger Hilt
//    implementation(libs.google.hilt.android)
//    ksp(libs.google.hilt.compiler)
//    // For Robolectric tests.
//    testImplementation(libs.google.hilt.testing)
//    // ...with Kotlin.
//    kspTest(libs.google.hilt.compiler)
//    // For instrumented tests.
//    androidTestImplementation(libs.google.hilt.testing)
//    // ...with Kotlin.
//    kspAndroidTest(libs.google.hilt.compiler)

    // ----- Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}