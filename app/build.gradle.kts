import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.ncs.android.application)
    alias(libs.plugins.ncs.android.compose)
    alias(libs.plugins.ncs.hilt)
    alias(libs.plugins.google.oss)
    alias(libs.plugins.google.service)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.ccc.ncs"

    defaultConfig {
        applicationId = "com.ccc.ncs"
        versionCode = 27
        versionName = "0.2.21"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                storeFile = file(keystoreProperties["RELEASE_KEY_STORE"] as String)
                storePassword = keystoreProperties["RELEASE_KEY_PASSWORD"] as String
                keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"] as String
                keyPassword = keystoreProperties["RELEASE_KEY_ALIAS_PASSWORD"] as String
            } else {
                println("Warning: keystore.properties file not found. Release signing configuration will not be set.")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            /*
            The application could not be installed: INSTALL_BASELINE_PROFILE_FAILED Installation failed due to: 'Baseline profile did not install
            같은 applicationId에 다른 singing key로 설치 될 때 발생하는 오류
            https://stackoverflow.com/questions/79111518/why-do-i-get-install-baseline-profile-failed-when-running-the-app
             */
            applicationIdSuffix = ".debug"
//            isMinifyEnabled = true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        disable.add("Instantiatable")
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":designsystem"))
    implementation(project(":playback"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)

    implementation(libs.paing.runtime)
    implementation(libs.paing.compose)

    implementation(libs.reorderable)
    implementation(libs.lottie.compose)
    implementation(libs.google.oss)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.androidx.core.splashscreen)
}