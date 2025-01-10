enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        // For Google Maps Secrets Gradle Plugin
        id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin").version("2.0.1")

        // For Google Services (Analytics, Feedback)
        id("com.google.gms.google-services").version("4.4.0")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WialonLocal"
include(":androidApp")
include(":composeApp")
