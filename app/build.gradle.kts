plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.uacm.cm.puntodeventa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uacm.cm.puntodeventa"
        minSdk = 22
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.Spikeysanju:MotionToast:1.4")//toast
    implementation ("io.github.tutorialsandroid:kalertdialog:20.4.8")//popup
    implementation ("com.github.TutorialsAndroid:progressx:v6.0.19")//popup
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.android.material:material:1.12.0-alpha02")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.daimajia.androidanimations:library:2.4@aar")
}