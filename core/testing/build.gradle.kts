plugins {
    alias(libs.plugins.igozogo.android.library)
}

android {
    namespace = "io.jacob.igozogo.core.testing"

    packaging.resources {
        excludes += "/META-INF/LICENSE.md"
        excludes += "/META-INF/LICENSE-notice.md"
    }
}

dependencies {
    implementation(projects.core.model)

    // ----- Test
    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.mockk)
}