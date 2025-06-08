import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.vic.project.app_news"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vic.project.app_news"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
}

extensions.configure<KspExtension> {
    arg("room.generateKotlin", "true")
}

extensions.configure<RoomExtension> {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.gradle.plugin)
    implementation(libs.androidx.startup.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // coil
    implementation(libs.coil.compose)


    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)


    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)


    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.dagger.hilt.android)
    ksp (libs.dagger.hilt.compiler)


    implementation(libs.timber)
    implementation(libs.gson)
    implementation(libs.kotlinx.serialization.json)

    //navigate
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    //constraintlayout in compose
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.animation.core.android)
}