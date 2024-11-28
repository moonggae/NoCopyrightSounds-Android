plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.network"
}

dependencies {
    implementation(project(":model"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.jsoup)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}