pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ClimbingRecord"
include(":app")
include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:mvi")
include(":core:navigation")
include(":core:ui")
include(":data")
include(":domain")
include(":feature:dashboard")
include(":presentation:main")
include(":core:network")
include(":core:testing")
