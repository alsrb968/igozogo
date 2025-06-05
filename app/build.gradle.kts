plugins {
    alias(libs.plugins.igozogo.android.application)
    alias(libs.plugins.igozogo.android.application.compose)
    alias(libs.plugins.igozogo.android.hilt)
    alias(libs.plugins.igozogo.android.test)
}

android {
    namespace = "io.jacob.igozogo"

    defaultConfig {
        applicationId = "io.jacob.igozogo"
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
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.design)
    implementation(projects.feature.home)
    implementation(projects.feature.bookmark)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)

    //----- Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.animation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.foundation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}