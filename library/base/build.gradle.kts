plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
}

android {
    namespace = "com.lee.bb.base"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}