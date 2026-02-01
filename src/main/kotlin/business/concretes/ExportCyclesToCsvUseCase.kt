package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.dataAccess.CycleRepository

class ExportCyclesToCsvUseCase(private val cycleRepository: CycleRepository) : UseCase<Long, DataResult<String>> {

    override suspend fun execute(request: Long): DataResult<String> {
        val cycles = cycleRepository.findAll()
            .filter { it.userId == request }
            .sortedBy { it.startDate }

        val csvBuilder = StringBuilder()
        csvBuilder.append("ID,Baslangic Tarihi,Bitis Tarihi,Notlar\n") // Header

        cycles.forEach {
            csvBuilder.append("${it.id},${it.startDate},${it.endDate ?: "-"},${it.note ?: ""}\n")
        }

        return SuccessDataResult(csvBuilder.toString(), "CSV verisi oluşturuldu.")
    }

    override fun createError(message: String): DataResult<String> = ErrorDataResult(null, message)
}