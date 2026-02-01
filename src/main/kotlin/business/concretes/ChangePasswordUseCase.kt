package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.flowalora.business.dtos.requests.ChangePasswordRequest
import org.flowalora.dataAccess.UserRepository
import kotlin.time.Clock
import org.erbalkan.kernel.utilities.results.Result


class ChangePasswordUseCase(private val userRepository: UserRepository) :
    UseCase<Pair<Long, ChangePasswordRequest>, Result> {

    override suspend fun execute(request: Pair<Long, ChangePasswordRequest>): Result {
        val (userId, req) = request
        val user = userRepository.findById(userId) ?: return Result(false, "Kullanıcı bulunamadı.")

        // Şifre Doğrulama (Gerçek projede BCrypt.checkPassword olmalı)
        if (user.passwordHash != req.currentPassword) {
            return Result(false, "Mevcut şifreniz hatalı.")
        }

        val updatedUser = user.copy(
            passwordHash = req.newPassword,
            updatedAt = Clock.System.now()
        )

        userRepository.update(updatedUser)
        return Result(true, "Şifreniz başarıyla değiştirildi.")
    }

    override fun createError(message: String): Result = Result(false, message)
}