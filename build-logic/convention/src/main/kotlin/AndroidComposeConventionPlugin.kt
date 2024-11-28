
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.ccc.ncs.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            plugins.withId("com.android.application") {
                configureAndroidCompose(extensions.getByType<ApplicationExtension>())
            }

            plugins.withId("com.android.library") {
                configureAndroidCompose(extensions.getByType<LibraryExtension>())
            }
        }
    }
}