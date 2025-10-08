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
    implementation(project(":library:navermap"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:presenter"))
    implementation(project(":feature:home"))
    implementation(project(":feature:search"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splash)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
}