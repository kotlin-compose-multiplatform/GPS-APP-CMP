import com.google.gms.googleservices.GoogleServicesTask


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    // for Google Maps API key secrets gradle plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")



    id("com.google.gms.google-services")
    // Google Services - for feedback
//    alias(libs.plugins.google.firebase.crashlytics)
}


project.afterEvaluate {
    tasks.withType<GoogleServicesTask> {
        gmpAppId.set(File(project.buildDir, "${name}-gmpAppId.txt"))
    }
}

android {
    namespace = "com.gs.wialonlocal.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.gs.wialonlocal.android"
        minSdk = 21
        targetSdk = 34
        versionCode = 13
        versionName = "1.0.3"
    }

    signingConfigs {
        create("release") {
            keyAlias = "jetbrains"
            keyPassword = "changeme"
            storeFile = file("jetbrains.jks")
            storePassword = "changeme"
        }
    }

    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
//    implementation(libs.firebase.crashlytics)
    debugImplementation(libs.compose.ui.tooling)

    // in app updates
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
}