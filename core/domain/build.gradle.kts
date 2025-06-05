plugins {
    alias(libs.plugins.igozogo.android.library)
}

android {
    namespace = "io.jacob.igozogo.core.domain"
}

dependencies {
    implementation(libs.inject)

    implementation(libs.androidx.paging.common)

    // ----- Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}