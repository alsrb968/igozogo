import io.jacob.igozogo.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
//            apply(plugin = "com.android.test")
            apply(plugin = "org.jetbrains.kotlin.android")

//            extensions.configure<TestExtension> {
//                configureKotlinAndroid(this)
//                defaultConfig.targetSdk = 35
//            }

            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "androidTestImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "androidTestImplementation"(libs.findLibrary("mockk.android").get())
            }
        }
    }
}
