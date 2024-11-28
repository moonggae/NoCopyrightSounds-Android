plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.android.compose)
}

android {
    namespace = "com.ccc.ncs.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.coil.kt.compose)
}