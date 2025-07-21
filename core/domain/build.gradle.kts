plugins {
    id("crowdtracker.android.library.convention")
    id("crowdtracker.android.hilt.library.convention")
}

android {
    namespace = "com.lee.crowdtracker.domain"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:data-impl"))

    // Test
    testImplementation(project(":library:test"))
}