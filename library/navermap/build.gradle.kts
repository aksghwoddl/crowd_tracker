plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.compose.library.convention")
    id("crowdtracker.android.hilt.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.libray.navermap"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(libs.naver.map.sdk)
    implementation(libs.play.services.location)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(project(":library:base"))
    implementation(project(":library:design"))
    implementation(project(":core:domain"))

    testImplementation(project(":library:test"))
}
