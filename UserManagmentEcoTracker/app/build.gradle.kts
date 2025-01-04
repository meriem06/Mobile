plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.usermanagmentecotracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.usermanagmentecotracker"
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

    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {
    // Core dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.recyclerview)
    annotationProcessor(libs.room.compiler)

    // Play Services
    implementation(libs.play.services.safetynet)


    // Retrofit and Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.anastr:speedviewlib:1.6.1") // library waeel
    // JavaMail API from Maven Central
   // implementation("com.sun.mail:javax.mail:1.6.2")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("org.osmdroid:osmdroid-android:6.1.10")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.android.material:material:1.9.0")

    // Unit testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Remove manual jar dependencies
    // These lines are removed because they caused conflicts:
    // implementation(files("src/main/java/com/example/usermanagmentecotracker/Libs/activation.jar"))
    // implementation(files("src/main/java/com/example/usermanagmentecotracker/Libs/additional.jar"))
    // implementation(files("src/main/java/com/example/usermanagmentecotracker/Libs/mail.jar"))
}
