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
    implementation(project(":cache"))
    implementation(project(":domain"))
    implementation(project(":playback"))
    androidTestImplementation(project(":network"))

    implementation(libs.androidx.core.ktx)

    androidTestImplementation(project(":database-test"))

    implementation(libs.paing.runtime)
    implementation(libs.paing.compose)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.okhttp.logging)
}