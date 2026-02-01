package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.dtos.requests.UpdateCycleRequest
import org.flowalora.business.mappers.CycleMapper
import org.flowalora.dataAccess.CycleRepository
import org.flowalora.entities.CycleDto
import kotlin.time.Clock
import kotlin.time.Instant

class UpdateCycleUseCase(
    private val cycleRepository: CycleRepository,
    private val cycleMapper: CycleMapper
) : UseCase<Pair<Long, UpdateCycleRequest>, DataResult<CycleDto>> {

    override suspend fun execute(request: Pair<Long, UpdateCycleRequest>): DataResult<CycleDto> {
        val (userIdFromToken, updateReq) = request

        // 1. Kayıt var mı kontrol et
        val existingCycle = cycleRepository.findById(updateReq.cycleId)
            ?: return createError("Güncellenecek kayıt bulunamadı.")

        // 2. Güvenlik: Bu kayıt bu kullanıcıya mı ait?
        if (existingCycle.userId != userIdFromToken) {
            return createError("Bu kaydı güncelleme yetkiniz yok.")
        }

        // 3. Güncelleme işlemi
        val updatedEntity = existingCycle.copy(
            endDate = updateReq.endDate?.let { Instant.parse(it) },
            note = updateReq.note ?: existingCycle.note,
            updatedAt = Clock.System.now()
        )

        val saved = cycleRepository.update(updatedEntity)
        return SuccessDataResult(cycleMapper.toDto(saved), "Döngü başarıyla güncellendi.")
    }

    override fun createError(message: String): DataResult<CycleDto> = ErrorDataResult<CycleDto>(null, message)
}