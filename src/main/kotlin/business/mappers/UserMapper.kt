package org.flowalora.business.mappers

import org.erbalkan.kernel.utilities.mappers.concretes.BaseMapper
import org.flowalora.entities.User
import org.flowalora.entities.UserDto

class UserMapper : BaseMapper<User, UserDto, Long>() {
    override fun toDto(entity: User): UserDto = UserDto(
        id = entity.id,
        email = entity.email,
        fullName = entity.fullName
    )

    override fun toEntity(dto: UserDto): User {
        // Kayıt işlemleri genellikle özel Request modelleriyle (RegisterRequest) yapıldığı için
        // bu metod genelde kullanılmaz veya özel bir dönüşüm gerektirir.
        throw UnsupportedOperationException("Kayıt için lütfen RegisterRequest kullanın.")
    }
}