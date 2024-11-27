import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class BaseConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(CommonExtension::class.java)

        android.buildFeatures.buildConfig = true

        android.defaultConfig {
            AppConfig.getBuildConfigFields().forEach { field ->
                buildConfigField("String", field.name, "\"${project.findProperty(field.name) ?: field.defaultValue}\"")
            }
        }
    }
}