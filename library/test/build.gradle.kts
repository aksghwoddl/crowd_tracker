plugins {
    id("crowdtracker.android.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.test"
}

dependencies {
    api(libs.bundles.unit.test)
}