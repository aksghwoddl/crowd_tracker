import com.android.build.api.dsl.LibraryExtension
import convention.buildConfigConfiguration
import convention.kotlinAndroidConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.plugin.serialization")
        }
        extensions.configure<LibraryExtension> {
            kotlinAndroidConfiguration(this)
            buildConfigConfiguration(this)
        }
    }
}