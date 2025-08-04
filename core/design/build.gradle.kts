plugins {
    alias(libs.plugins.igozogo.android.library)
    alias(libs.plugins.igozogo.android.library.compose)
    alias(libs.plugins.igozogo.android.test)
}

android {
    namespace = "io.jacob.igozogo.core.design"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.testing)
    implementation(projects.core.model)

    //----- Coil
    implementation(libs.coil.compose)

    //----- Paging
    implementation(libs.androidx.paging.compose)
}