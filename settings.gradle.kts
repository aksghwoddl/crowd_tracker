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
include(":library")
include(":library:design-system")
include(":library:design")
include(":core:data")
include(":core:data-impl")
include(":core:domain")
include(":library:base")
include(":library:test")
include(":core:presenter")
include(":root")
include(":feature")
include(":feature:search")
include(":feature:home")
