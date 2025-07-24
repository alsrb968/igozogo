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

rootProject.name = "Igozogo"

include(":app")

include(":core:data")
include(":core:domain")
include(":core:design")

include(":feature:home")
include(":feature:search")
include(":feature:bookmark")
include(":feature:setting")
include(":feature:placedetail")
include(":feature:storydetail")
include(":feature:player")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
