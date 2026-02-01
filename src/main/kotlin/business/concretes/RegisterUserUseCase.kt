package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.RegisterRequest
import org.flowalora.business.mappers.UserMapper
import org.flowalora.dataAccess.UserRepository
import org.flowalora.entities.User
import org.flowalora.entities.UserDto
import kotlin.time.Clock

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UseCase<RegisterRequest, DataResult<UserDto>> {

    // 1. Suspend olarak işaretledik (Kernel interface'i ile uyumlu)
    override suspend fun execute(request: RegisterRequest): DataResult<UserDto> {

        // İş Kuralı Örneği: Email kontrolü vb. burada yapılabilir.

        val newUser = User(
            email = request.email,
            passwordHash = request.password, // Gerçek senaryoda Hashlenmeli
            fullName = request.fullName,
            createdAt = Clock.System.now()
        )

        val savedUser = userRepository.insert(newUser)
        return SuccessDataResult(userMapper.toDto(savedUser), "Kullanıcı başarıyla kaydedildi.")
    }

    // 2. Hata dönerken tip uyuşmazlığı yaşamamak için jeneriği <UserDto> olarak belirtiyoruz
    override fun createError(message: String): DataResult<UserDto> {
        // null data içeren ama tipi DataResult<UserDto> olan bir hata dönüyoruz.
        return ErrorDataResult<UserDto>(null, message)
    }
}