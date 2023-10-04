pluginManagement {
    repositories {
        google()
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

rootProject.name = "Focused"
include(":app")
include(":core:model")
include(":domain")
include(":core:common")
include(":core:ui-design")
include(":core:ui-common")
include(":data-repository")
include(":datastore")
include(":data-local")
include(":core:di")
include(":feature:onboarding")
include(":feature:tasks")
include(":feature:projects")
include(":feature:timer")
include(":feature:report")
include(":core:notification")
include(":core:service")
