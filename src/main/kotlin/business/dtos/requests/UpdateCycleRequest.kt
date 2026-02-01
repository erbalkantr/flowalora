package org.flowalora.business.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCycleRequest(
    val cycleId: Long,
    val endDate: String?,
    val note: String? = null
)