plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // api로 선언할 경우 다른 모듈에서도 사용할 수 있음
    api(libs.kotlinx.datetime)
}