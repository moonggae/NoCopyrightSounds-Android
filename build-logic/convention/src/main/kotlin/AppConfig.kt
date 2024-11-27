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