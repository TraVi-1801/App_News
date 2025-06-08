package com.vic.project.app_news.data

import com.vic.project.app_news.utils.AppConstants
import java.util.Calendar

object EnvironmentManager {
    var baseUrl : BuildEnvironment = BuildEnvironment.DOMAIN_DEV
    fun setDomain(buildEnvironment: BuildEnvironment) {
        baseUrl = buildEnvironment
    }

    val versionApp = "App \u00A9 ${
        Calendar.getInstance().get(Calendar.YEAR)
    } \u25CF ${baseUrl.environmentName}_${AppConstants.VERSION_NAME}"
}

enum class BuildEnvironment(
    val url_v1: String,
    val position: Int,
    val environmentName : String,
) {
    DOMAIN_DEV(
        "https://newsapi.org/v2/",
        0,
        environmentName = "Dev",
    ),
    DOMAIN_STAGING(
        "",
        1,
        environmentName = "Staging",
    ),
    DOMAIN_LIVE(
        "",
        2,
        environmentName = "Live",
    );

    companion object {
        fun filter(position: Int): BuildEnvironment {
            for (environment in values()) {
                if (position == environment.position) {
                    return environment
                }
            }
            return DOMAIN_DEV
        }
    }
}