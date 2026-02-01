package org.flowalora.business.mappers

import org.erbalkan.kernel.utilities.mappers.concretes.BaseMapper
import org.flowalora.entities.Cycle
import org.flowalora.entities.CycleDto

class CycleMapper : BaseMapper<Cycle, CycleDto, Long>() {
    override fun toDto(entity: Cycle): CycleDto = CycleDto(
        id = entity.id,
        startDate = entity.startDate.toString(), // ISO-8601
        endDate = entity.endDate?.toString(),
        note = entity.note
    )

    override fun toEntity(dto: CycleDto): Cycle {
        // Bu örnekte doğrudan Entity oluşturmak yerine Request modelini tercih ediyoruz.
        throw UnsupportedOperationException("Lütfen yeni kayıt için AddCycleRequest kullanın.")
    }
}