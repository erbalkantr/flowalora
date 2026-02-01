package org.flowalora.api.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*

fun Application.configureSwagger() {
    routing {
        // Hata veren bloğu sildik, sadece path ve dosya yolunu bıraktık
        swaggerUI(path = "swagger", swaggerFile = "src/main/resources/openapi/documentation.yaml")

        // openAPI(path = "openapi", swaggerFile = "src/main/resources/openapi/documentation.yaml")
    }
}