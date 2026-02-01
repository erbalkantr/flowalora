package org.flowalora.dataAccess

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.timestamp

object Users : LongIdTable("users") {
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val fullName = varchar("full_name", 100)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()
}

object Cycles : LongIdTable("cycles") {
    val userId = reference("user_id", Users)
    val startDate = timestamp("start_date")
    val endDate = timestamp("end_date").nullable()
    val note = text("note").nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").nullable()
}