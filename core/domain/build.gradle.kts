plugins {
    alias(libs.plugins.igozogo.android.library)
    alias(libs.plugins.igozogo.android.library.jacoco)
}

android {
    namespace = "io.jacob.igozogo.core.domain"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.inject)

    implementation(libs.androidx.paging.common)

    // ----- Test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    testImplementation(projects.core.testing)
}