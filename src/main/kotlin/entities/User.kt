package org.flowalora.entities

import kotlinx.serialization.Serializable
import org.erbalkan.kernel.entities.Dto
import org.erbalkan.kernel.entities.Entity
import kotlin.time.Instant

// Veritabanı Varlığı
data class User(
    override val id: Long? = null,
    val email: String,
    val passwordHash: String,
    val fullName: String,
    override val createdAt: Instant,
    override var updatedAt: Instant? = null
) : Entity<Long>

// API Transfer Nesnesi
@Serializable
data class UserDto(
    val id: Long?,
    val email: String,
    val fullName: String
) : Dto