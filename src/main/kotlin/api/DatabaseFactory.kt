package org.flowalora.api

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flowalora.dataAccess.Cycles
import org.flowalora.dataAccess.Users
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {

    fun init() {
        // Docker ve Local uyumu
        val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/flowalora_db"
        val dbUser = System.getenv("DB_USER") ?: "postgres"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "your_password"

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            maximumPoolSize = 10
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        // Başlangıç tablolarını oluştur (Senkron transaction burada hala en güvenlisi)
        transaction {
            SchemaUtils.create(Users, Cycles)
        }
    }

    /**
     * İşte beklenen modern yapı:
     * 1. withContext(Dispatchers.IO) ile I/O thread'ine geçiyoruz.
     * 2. suspendTransaction { } ile veritabanı işlemini yürütüyoruz.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            suspendTransaction {
                block()
            }
        }
}

