plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.compose.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.libray.design"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}