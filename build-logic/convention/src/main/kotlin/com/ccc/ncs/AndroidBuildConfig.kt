package com.ccc.ncs

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureAndroidBuildConfig(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            buildConfig = true
        }

        defaultConfig {
            AppConfig.getBuildConfigFields().forEach { field ->
                buildConfigField(
                    type = "String",
                    name = field.name,
                    value = "\"${findProperty(field.name) ?: field.defaultValue}\""
                )
            }
        }
    }
}

object AppConfig {
    const val WEB_URL = "https://ncs.io"
    const val FANDOM_URL = "https://nocopyrightsounds.fandom.com"
    const val FIREBASE_ANALYTICS_ENABLED = true

    fun getBuildConfigFields() = listOf(
        BuildConfigField("WEB_URL", WEB_URL),
        BuildConfigField("FANDOM_URL", FANDOM_URL),
        BuildConfigField("FIREBASE_ANALYTICS_ENABLED", FIREBASE_ANALYTICS_ENABLED.toString())
    )
}

data class BuildConfigField(
    val name: String,
    val defaultValue: String
)