plugins {
    alias(libs.plugins.igozogo.android.feature)
    alias(libs.plugins.igozogo.android.library.compose)
    alias(libs.plugins.igozogo.android.test)
}

android {
    namespace = "io.jacob.igozogo.feature.home"
}

dependencies {
//    implementation(libs.androidx.core.ktx)

    //----- Coil
    implementation(libs.coil.compose)

    //----- Paging
    implementation(libs.androidx.paging.compose)
}