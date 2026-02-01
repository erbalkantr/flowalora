package org.flowalora.business.concretes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.AuthDto
import org.flowalora.business.dtos.requests.LoginRequest
import org.flowalora.business.mappers.UserMapper
import org.flowalora.dataAccess.UserRepository
import java.util.Date

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UseCase<LoginRequest, DataResult<AuthDto>> {

    override suspend fun execute(request: LoginRequest): DataResult<AuthDto> {
        // 1. Kullanıcıyı bul (UserRepository'ye findByEmail eklediğimizi varsayalım)
        val user = userRepository.findAll().find { it.email == request.email }
            ?: return createError("Kullanıcı bulunamadı.")

        // 2. Şifre kontrolü (Basit tutuyoruz, gerçekte BCrypt kullanılmalı)
        if (user.passwordHash != request.password) return createError("Hatalı şifre.")

        // 3. JWT Token Üretimi
        val token = JWT.create()
            .withAudience("reforflowers-api")
            .withIssuer("reforflowers.org")
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000)) // 1 Saat
            .sign(Algorithm.HMAC256("super-secret-key")) // Gizli anahtar

        val authDto = AuthDto(token, userMapper.toDto(user))
        return SuccessDataResult(authDto, "Giriş başarılı.")
    }

    override fun createError(message: String): DataResult<AuthDto> = ErrorDataResult<AuthDto>(null, message)
}