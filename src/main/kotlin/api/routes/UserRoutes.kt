package org.flowalora.api.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.erbalkan.kernel.business.builders.decorate
import org.flowalora.business.concretes.ChangePasswordUseCase
import org.flowalora.business.concretes.LoginUserUseCase
import org.flowalora.business.concretes.RegisterUserUseCase
import org.flowalora.business.concretes.UpdateProfileUseCase
import org.flowalora.business.dtos.requests.ChangePasswordRequest
import org.flowalora.business.dtos.requests.LoginRequest
import org.flowalora.business.dtos.requests.RegisterRequest
import org.flowalora.business.dtos.requests.UpdateProfileRequest
import org.flowalora.business.mappers.UserMapper
import org.flowalora.dataAccess.UserRepository
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val registerUseCase by inject<RegisterUserUseCase>()
    val loginUseCase by inject<LoginUserUseCase>()
    val userRepository by inject<UserRepository>()
    val userMapper by inject<UserMapper>()
    val updateProfileUseCase by inject<UpdateProfileUseCase>()
    val changePasswordUseCase by inject<ChangePasswordUseCase>()
    // Bağımlılıkları manuel yönetiyoruz (İleride DI eklenebilir)
//    val userRepository = UserRepository()
//    val userMapper = UserMapper()
//
//    val updateProfileUseCase = UpdateProfileUseCase(userRepository, userMapper).decorate().withLogging().withTransaction().build()
//    val changePasswordUseCase = ChangePasswordUseCase(userRepository).decorate().withLogging().withTransaction().build()
//
//    // UseCase'i senin Builder'ınla inşa ediyoruz
//    val registerUseCase = RegisterUserUseCase(userRepository, userMapper)
//        .decorate()
//        .withLogging()
//        .withTransaction()
//        .withRule { req -> if (req.password.length < 8) "Şifre çok kısa!" else null }
//        .withRule { req -> if (!req.email.contains("@")) "Geçersiz e-posta!" else null }
//        .build()
//
//    // 2. Giriş UseCase'i
//    val loginUseCase = LoginUserUseCase(userRepository, userMapper)
//        .decorate()
//        .withLogging()
//        .build()


    route("/auth") {
// POST http://localhost:8080/auth/register
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val result = registerUseCase.execute(request)
            call.respond(if (result.success) HttpStatusCode.Created else HttpStatusCode.BadRequest, result)
        }
        // POST http://localhost:8080/auth/login
        post("/login") {
            val request = call.receive<LoginRequest>()
            val result = loginUseCase.execute(request)

            if (result.success) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.Unauthorized, result)
            }
        }
    }

    authenticate("auth-jwt") {
        route("/profile") {

            // GET http://localhost:8080/profile/me (Kendi bilgilerini getir)
            get("/me") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
                val user = userRepository.findById(userId)
                if (user != null) call.respond(userMapper.toDto(user)) else call.respond(HttpStatusCode.NotFound)
            }

            // PUT http://localhost:8080/profile/update
            put("/update") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
                val request = call.receive<UpdateProfileRequest>()
                call.respond(updateProfileUseCase.execute(Pair(userId, request)))
            }

            // POST http://localhost:8080/profile/change-password
            post("/change-password") {
                val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
                val request = call.receive<ChangePasswordRequest>()
                call.respond(changePasswordUseCase.execute(Pair(userId, request)))
            }
        }
    }
}