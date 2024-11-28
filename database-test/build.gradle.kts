plugins {
    alias(libs.plugins.ncs.android.library)
}

android {
    namespace = "com.ccc.ncs.database.test"
}

dependencies {
    implementation(project(":model"))
    implementation(project(":database"))
}