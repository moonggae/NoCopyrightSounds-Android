plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.cache"
}

dependencies {
    implementation(project(":datastore"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.media3)
}