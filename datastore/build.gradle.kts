plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.datastore"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.datastore.preferences)
}