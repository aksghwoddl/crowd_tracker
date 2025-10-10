plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
    id("crowdtracker.android.compose.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.feature.search"
}

dependencies {
    implementation(project(":core:presenter"))
    implementation(project(":core:domain"))
    implementation(project(":library:base"))
    implementation(project(":library:design"))

    testImplementation(project(":library:test"))
}