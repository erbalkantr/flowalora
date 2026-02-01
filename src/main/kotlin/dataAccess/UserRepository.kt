package org.flowalora.dataAccess

import org.erbalkan.kernel.dataAccess.concretes.exposed.ExposedRepositoryTemplate
import org.flowalora.entities.User
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder

class UserRepository : ExposedRepositoryTemplate<User, Long, Users>(Users) {

    override fun rowToEntity(row: ResultRow): User = User(
        id = row[Users.id].value,
        email = row[Users.email],
        passwordHash = row[Users.passwordHash],
        fullName = row[Users.fullName],
        createdAt = row[Users.createdAt],
        updatedAt = row[Users.updatedAt]
    )

    override fun Users.mapToTable(statement: UpdateBuilder<*>, entity: User) {
        statement[email] = entity.email
        statement[passwordHash] = entity.passwordHash
        statement[fullName] = entity.fullName
        statement[createdAt] = entity.createdAt
        statement[updatedAt] = entity.updatedAt
    }
}