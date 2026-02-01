package org.flowalora.business.dtos.requests

import kotlinx.serialization.Serializable
import org.flowalora.entities.UserDto

@Serializable
data class LoginRequest(val email: String, val password: String)

// Başarılı girişte dönecek veri
@Serializable
data class AuthDto(val token: String, val user: UserDto)