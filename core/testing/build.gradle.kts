plugins {
    alias(libs.plugins.igozogo.android.library)
}

android {
    namespace = "io.jacob.igozogo.core.testing"
}

dependencies {
    // ----- Test
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.mockk)
}