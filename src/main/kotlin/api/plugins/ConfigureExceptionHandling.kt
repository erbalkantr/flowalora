package org.flowalora.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.* // Kritik import!
import io.ktor.server.response.*
import org.erbalkan.kernel.utilities.results.ErrorDataResult

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        // 1. Beklenmedik tüm hataları (Exception) yakala
        exception<Throwable> { call, cause ->
            // Loglama yapabiliriz ama sadece konsola yazsın yeter (Logging'i iptal ettiğimiz için)
            cause.printStackTrace()

            val errorResponse = ErrorDataResult<Unit>(
                resultData = null,
                message = "Sunucu tarafında beklenmedik bir hata oluştu: ${cause.localizedMessage}"
            )

            call.respond(HttpStatusCode.InternalServerError, errorResponse)
        }

        // 2. Geçersiz JSON formatı gibi durumlar için (BadRequest)
        status(HttpStatusCode.BadRequest) { call, status ->
            val errorResponse = ErrorDataResult<Unit>(
                resultData = null,
                message = "Gönderilen veri formatı hatalı veya eksik."
            )
            call.respond(status, errorResponse)
        }
    }
}