package org.flowalora.business.concretes

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.dataAccess.CycleRepository

class PredictNextCycleUseCase(private val cycleRepository: CycleRepository) : UseCase<Long, DataResult<String>> {

    override suspend fun execute(request: Long): DataResult<String> {
        val cycles = cycleRepository.findAll()
            .filter { it.userId == request }
            .sortedByDescending { it.startDate }

        if (cycles.size < 2) return createError("Tahmin için en az 2 geçmiş kayıt gerekli.")

        // Ortalama döngü süresini hesapla (Gün bazında)
        val intervals = mutableListOf<Long>()
        for (i in 0 until cycles.size - 1) {
            val diff = cycles[i].startDate - cycles[i+1].startDate
            intervals.add(diff.inWholeDays)
        }

        val averageCycleDays = intervals.average().toLong()
        val nextDate = cycles.first().startDate.plus(averageCycleDays, DateTimeUnit.DAY, TimeZone.UTC)

        return SuccessDataResult(
            nextDate.toString(),
            "Bir sonraki beklenen tarih: $nextDate (Ortalama $averageCycleDays gün)"
        )
    }

    override fun createError(message: String): DataResult<String> = ErrorDataResult<String>(null, message)
}