plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.gestion_activityrecognition"
    compileSdk = 34
    packaging {
        resources {
            resources.excludes.add("META-INF/NOTICE.md")
            resources.excludes.add("META-INF/LICENSE.md")        }
    }
    defaultConfig {
        applicationId = "com.example.gestion_activityrecognition"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.5.0")
    implementation("com.airbnb.android:lottie:6.1.0")
    // TODO: Review play services library required for activity recognition.
    implementation ("com.google.android.gms:play-services-location:19.0.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
    implementation (libs.android.mail)
    implementation (libs.android.activation)
}
