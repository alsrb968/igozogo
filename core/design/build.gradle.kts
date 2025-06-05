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

//    implementation(libs.timber)

//    implementation(libs.androidx.core.ktx)

    //----- Compose
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.bundles.androidx.compose)
//    debugImplementation(libs.bundles.androidx.compose.debug)

    //----- Coil
    implementation(libs.coil.compose)

    //----- Paging
    implementation(libs.androidx.paging.compose)

    // ----- Test
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    testImplementation(libs.kotlinx.coroutines.test)
//    androidTestImplementation(libs.kotlinx.coroutines.test)

//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}