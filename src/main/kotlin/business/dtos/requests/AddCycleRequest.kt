package org.flowalora.business.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class AddCycleRequest(
    val userId: Long,
    val startDate: String, // "2026-01-29T10:00:00Z" formatında bekliyoruz
    val note: String? = null
)