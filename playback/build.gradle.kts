plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.playback"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":cache"))
    implementation(project(":analytics"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.coroutines.guava)
    implementation(libs.bundles.media3)
}