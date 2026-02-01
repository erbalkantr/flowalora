package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.AddCycleRequest
import org.flowalora.business.mappers.CycleMapper
import org.flowalora.dataAccess.CycleRepository
import org.flowalora.entities.Cycle
import org.flowalora.entities.CycleDto
import kotlin.time.Clock
import kotlin.time.Instant

class AddCycleUseCase(
    private val cycleRepository: CycleRepository,
    private val cycleMapper: CycleMapper
) : UseCase<AddCycleRequest, DataResult<CycleDto>> {

    override suspend fun execute(request: AddCycleRequest): DataResult<CycleDto> {
        val newCycle = Cycle(
            userId = request.userId,
            startDate = Instant.parse(request.startDate),
            endDate = null, // Henüz bitmediği varsayımıyla
            note = request.note,
            createdAt = Clock.System.now()
        )

        val savedCycle = cycleRepository.insert(newCycle)
        return SuccessDataResult(cycleMapper.toDto(savedCycle), "Döngü kaydı başarıyla eklendi.")
    }

    override fun createError(message: String): DataResult<CycleDto> {
        return ErrorDataResult<CycleDto>(null, message)
    }
}