plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
}

android {
    namespace = "com.ccc.ncs.data"
}

dependencies {
    implementation(project(":network"))
    implementation(project(":database"))
    implementation(project(":datastore"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.paing.runtime)
    implementation(libs.paing.compose)

    testImplementation(project(":database-test"))
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}