plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.data"
}

dependencies {
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation(libs.retrofit)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)

    // Coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)


    // Preferences DataStore
    implementation(libs.datastore.preferences)

    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
}