package convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.composeConfiguration(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    with(commonExtension) {
        buildFeatures {
            compose = true
        }
    }

    dependencies {
        add("implementation", libs.findLibrary("activity.compose").get())
        add("implementation", libs.findLibrary("compose.ui").get())
        add("implementation", libs.findLibrary("compose.ui.graphics").get())
        add("implementation", libs.findLibrary("compose.ui.tooling.preview").get())
        add("implementation", libs.findLibrary("compose.material3").get())
        add("implementation", platform(libs.findLibrary("compose.bom").get()))
    }
}