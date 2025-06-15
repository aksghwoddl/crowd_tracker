pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CrowdTracker"

include(":app")
include(":core")
include(":feature")
include(":library")
include(":library:design-system")
include(":feature:main")
include(":library:design")
include(":core:data")
include(":core:data-impl")
include(":core:domain")
include(":library:base")
include(":library:test")
include(":core:presenter")
include(":root")
