package org.flowalora.dataAccess

import org.erbalkan.kernel.dataAccess.concretes.exposed.ExposedRepositoryTemplate
import org.flowalora.entities.Cycle
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder

class CycleRepository : ExposedRepositoryTemplate<Cycle, Long, Cycles>(Cycles) {

    override fun rowToEntity(row: ResultRow): Cycle = Cycle(
        id = row[Cycles.id].value,
        userId = row[Cycles.userId].value,
        startDate = row[Cycles.startDate],
        endDate = row[Cycles.endDate],
        note = row[Cycles.note],
        createdAt = row[Cycles.createdAt],
        updatedAt = row[Cycles.updatedAt]
    )

    override fun Cycles.mapToTable(statement: UpdateBuilder<*>, entity: Cycle) {
        statement[userId] = entity.userId
        statement[startDate] = entity.startDate
        statement[endDate] = entity.endDate
        statement[note] = entity.note
        statement[createdAt] = entity.createdAt
        statement[updatedAt] = entity.updatedAt
    }
}