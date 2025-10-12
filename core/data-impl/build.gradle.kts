plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.core.data.impl"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":library:base"))

    testImplementation(project(":library:test"))

    // Retrofit
    implementation(libs.retrofit)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)

    // OKHttp
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    // Coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
}