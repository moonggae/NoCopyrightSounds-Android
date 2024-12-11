plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.analytics"
}

dependencies {
    implementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.analytics)
}