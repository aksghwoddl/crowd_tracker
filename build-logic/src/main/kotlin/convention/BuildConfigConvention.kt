package convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.util.Properties

fun Project.buildConfigConfiguration(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = with(commonExtension) {
    val properties = Properties().apply {
        try {
            load(project.rootProject.file("local.properties").inputStream())
        } catch (_: Exception) {
        }
    }

    buildTypes {
        getByName("release") {
            buildConfigField("boolean", "APP_DEBUG", false.toString())
            buildConfigField(
                "String",
                "SEOUL_OPEN_API_KEY",
                properties["seoul_openapi_key"].toString()
            )
            buildConfigField(
                "String",
                "NAVER_CLIENT_ID",
                properties["naver-client-id"].toString()
            )
        }
        getByName("debug") {
            buildConfigField("boolean", "APP_DEBUG", true.toString())
            buildConfigField(
                "String",
                "SEOUL_OPEN_API_KEY",
                properties["seoul_openapi_key"].toString()
            )
            buildConfigField(
                "String",
                "NAVER_CLIENT_ID",
                properties["naver-client-id"].toString()
            )
        }
    }
}