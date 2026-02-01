package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.CycleStatsDto
import org.flowalora.dataAccess.CycleRepository

class GetCycleStatisticsUseCase(private val cycleRepository: CycleRepository) :
    UseCase<Long, DataResult<CycleStatsDto>> {

    override suspend fun execute(request: Long): DataResult<CycleStatsDto> {
        val cycles = cycleRepository.findAll()
            .filter { it.userId == request }
            .sortedByDescending { it.startDate }

        if (cycles.size < 2) return createError("İstatistik için en az 2 kayıt gerekli.")

        val intervals = mutableListOf<Long>()
        for (i in 0 until cycles.size - 1) {
            val diff = cycles[i].startDate - cycles[i+1].startDate
            intervals.add(diff.inWholeDays)
        }

        val stats = CycleStatsDto(
            averageCycleLength = intervals.average().toLong(),
            shortestCycle = intervals.minOrNull() ?: 0,
            longestCycle = intervals.maxOrNull() ?: 0,
            totalCyclesRecorded = cycles.size
        )

        return SuccessDataResult(stats, "İstatistikler başarıyla hesaplandı.")
    }

    override fun createError(message: String): DataResult<CycleStatsDto> = ErrorDataResult(null, message)
}