plugins {
    alias(libs.plugins.ncs.android.library)
    alias(libs.plugins.ncs.hilt)
    alias(libs.plugins.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.ccc.ncs.database"
}

dependencies {
    api(project(":model"))
    androidTestImplementation(project(":database-test"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.test.core.ktx)
    implementation(libs.gson)

    implementation(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.room.testing)
}