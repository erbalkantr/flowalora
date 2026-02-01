package org.flowalora.business.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class CycleStatsDto(
    val averageCycleLength: Long,
    val shortestCycle: Long,
    val longestCycle: Long,
    val totalCyclesRecorded: Int
)