plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.build.gradle)
    compileOnly(libs.gradle.hilt)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "crowdtracker.android.library.convention"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidHiltLibrary") {
            id = "crowdtracker.android.hilt.library.convention"
            implementationClass = "AndroidHiltLibraryConvention"
        }
    }
}