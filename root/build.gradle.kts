plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
    id("crowdtracker.android.compose.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.root"
}

dependencies {
    implementation(project(":library:design"))
    implementation(project(":library:base"))
    implementation(project(":core:domain"))
    implementation(project(":core:presenter"))
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}