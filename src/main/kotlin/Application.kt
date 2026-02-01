package org.flowalora

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.flowalora.api.DatabaseFactory
import org.flowalora.api.plugins.configureExceptionHandling
import org.flowalora.api.plugins.configureSwagger
import org.flowalora.api.routes.cycleRoutes
import org.flowalora.api.routes.userRoutes
import org.flowalora.di.appModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 1. Veritabanı Bağlantısı ve Tablo Oluşturma
    DatabaseFactory.init()
    // Hata yakalayıcıyı devreye al
    configureExceptionHandling()
    // Swagger'ı başlat
    configureSwagger()
    // Koin IOC
    install(Koin){
        slf4jLogger()
        modules(appModule)
    }

    // 2. JSON Desteği (Content Negotiation)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    // 3. Güvenlik Katmanı (JWT Authentication)
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "reforflowers"
            verifier(
                JWT.require(Algorithm.HMAC256("super-secret-key")) // Gerçek projede env variable olmalı
                    .withAudience("reforflowers-api")
                    .withIssuer("reforflowers.org")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asLong() != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }

    // 4. Rota Tanımlamaları
    routing {
        // Genel Rotalar (Kayıt, Giriş vb.)
        userRoutes()

        // Korumalı Rotalar (Döngü Takibi vb.)
        authenticate("auth-jwt") {
            cycleRoutes()
        }
    }
}