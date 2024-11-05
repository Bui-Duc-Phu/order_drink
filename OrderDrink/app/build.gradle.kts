plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.codes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.codes"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }
    packagingOptions {
        exclude ("META-INF/NOTICE.md")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    

    implementation ("com.google.android.material:material:1.11.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.android.gms:play-services-auth:21.1.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation(files("libs/activation.jar"))
    implementation(files("libs/mail.jar"))
    implementation(files("libs/additionnal.jar"))
    implementation("androidx.activity:activity:1.9.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-messaging:24.0.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation ("io.github.chaosleung:pinview:1.4.4")
    implementation ("de.hdodenhof:circleimageview:2.2.0")
    implementation ("com.saadahmedev.popup-dialog:popup-dialog:1.0.5")

    implementation ("com.airbnb.android:lottie:6.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("com.googlecode.libphonenumber:libphonenumber:8.12.36")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("commons-codec:commons-codec:1.15")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.saadahmedev.popup-dialog:popup-dialog:1.0.5")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.google.maps.android:android-maps-utils:2.2.6")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.itextpdf:itextg:5.5.10")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0-RC2")

    implementation("com.google.gms:google-services:4.4.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")



    implementation("androidx.core:core-animation:1.0.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.10")

    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")












}