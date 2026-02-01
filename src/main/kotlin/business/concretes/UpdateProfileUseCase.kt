package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.UpdateProfileRequest
import org.flowalora.business.mappers.UserMapper
import org.flowalora.dataAccess.UserRepository
import org.flowalora.entities.UserDto
import kotlin.time.Clock

class UpdateProfileUseCase(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UseCase<Pair<Long, UpdateProfileRequest>, DataResult<UserDto>> {

    override suspend fun execute(request: Pair<Long, UpdateProfileRequest>): DataResult<UserDto> {
        val (userId, updateReq) = request

        val user = userRepository.findById(userId) ?: return createError("Kullanıcı bulunamadı.")

        val updatedUser = user.copy(
            fullName = updateReq.fullName,
            updatedAt = Clock.System.now()
        )

        val saved = userRepository.update(updatedUser)
        return SuccessDataResult(userMapper.toDto(saved), "Profil başarıyla güncellendi.")
    }

    override fun createError(message: String): DataResult<UserDto> = ErrorDataResult(null, message)
}