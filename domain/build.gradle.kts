plugins {
    alias(libs.plugins.ncs.jvm.library)
}

dependencies {
    api(project(":model"))
    implementation(libs.coroutines.core)
}