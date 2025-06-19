plugins {
    id("crowdtracker.android.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.feature.main"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":library:design"))
}