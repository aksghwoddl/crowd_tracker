plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.build.gradle)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "crowdtracker.android.library.convention"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}