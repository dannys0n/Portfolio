plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.gpt_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gpt_app"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = true // Enable code shrinking with ProGuard or R8
            isShrinkResources = true // Enable resource shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            getByName("release") {
                buildConfigField("int", "CHAT_MODEL", "2") // 2 = gpt-4
            }
        }
        debug {
            isMinifyEnabled = true // Enable code shrinking with ProGuard or R8
            isShrinkResources = true // Enable resource shrinking
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            getByName("debug") {
                buildConfigField("int", "CHAT_MODEL", "1") // 1 = gpt-3.5-turbo
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true // Ensure BuildConfig is generated
    }
}

dependencies {

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.material3.android)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson) // Retrofit with Gson
    implementation(libs.material.icons.extended) // Add Material Icons Extended

    implementation(libs.error.prone.annotations.v2110)
    //debugImplementation(libs.error.prone.annotations)

    // Add Markwon dependencies
    implementation(libs.markwon.core)
    //implementation(libs.markwon.mathjax)
    //implementation(libs.markwon.)
    //implementation(libs.markwon.html)
    //implementation(libs.markwon.syntax.highlight)
    //implementation(libs.markwon.tables)

    //implementation(libs.markwon.katex)
}