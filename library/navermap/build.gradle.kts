plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.compose.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.libray.navermap"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.naver.map.sdk)
}