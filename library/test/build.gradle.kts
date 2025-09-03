plugins {
    id("crowdtracker.android.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.library.test"
}

dependencies {
    api(libs.bundles.unit.test)
}