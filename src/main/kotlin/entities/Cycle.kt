package org.flowalora.entities

import kotlinx.serialization.Serializable
import org.erbalkan.kernel.entities.Dto
import org.erbalkan.kernel.entities.Entity
import kotlin.time.Instant

// Veritabanı Varlığı
data class Cycle(
    override val id: Long? = null,
    val userId: Long,
    val startDate: Instant,
    val endDate: Instant?,
    val note: String?,
    override val createdAt: Instant,
    override var updatedAt: Instant? = null
) : Entity<Long>

// API Transfer Nesnesi
@Serializable
data class CycleDto(
    val id: Long?,
    val startDate: String, // API'de okunabilir string olarak dönecek
    val endDate: String?,
    val note: String?
) : Dto