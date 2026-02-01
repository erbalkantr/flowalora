package org.flowalora.business.concretes

import org.erbalkan.kernel.business.abstracts.UseCase
import org.erbalkan.kernel.utilities.results.DataResult
import org.erbalkan.kernel.utilities.results.ErrorDataResult
import org.erbalkan.kernel.utilities.results.SuccessDataResult
import org.flowalora.business.mappers.CycleMapper
import org.flowalora.dataAccess.CycleRepository
import org.flowalora.entities.CycleDto

class GetCyclesByUserUseCase(
    private val cycleRepository: CycleRepository,
    private val cycleMapper: CycleMapper
) : UseCase<Long, DataResult<List<CycleDto>>> {

    override suspend fun execute(request: Long): DataResult<List<CycleDto>> {
        // Repository'den tümünü çekip userId'ye göre filtreliyoruz
        // Not: Gerçek projede repository'e findByUserId eklemek daha performanslıdır.
        val userCycles = cycleRepository.findAll()
            .filter { it.userId == request }
            .map { cycleMapper.toDto(it) }

        return SuccessDataResult(userCycles, "Döngü geçmişi başarıyla getirildi.")
    }

    override fun createError(message: String): DataResult<List<CycleDto>> {
        return ErrorDataResult<List<CycleDto>>(null, message)
    }
}