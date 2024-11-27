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
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                // https://github.com/google/play-services-plugins/issues/223
                "com.google.android.gms.oss-licenses-plugin" -> useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Ncs"
include(":app")
include(":designsystem")
include(":data")
include(":model")
include(":network")
include(":database")
include(":database-test")
include(":playback")
include(":datastore")
include(":cache")
include(":domain")
