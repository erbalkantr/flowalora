package org.flowalora.api.routes

import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.response.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.erbalkan.kernel.business.builders.decorate
import org.flowalora.business.concretes.AddCycleUseCase
import org.flowalora.business.concretes.ExportCyclesToCsvUseCase
import org.flowalora.business.concretes.GetCycleStatisticsUseCase
import org.flowalora.business.concretes.GetCyclesByUserUseCase
import org.flowalora.business.concretes.PredictNextCycleUseCase
import org.flowalora.business.concretes.UpdateCycleUseCase
import org.flowalora.business.dtos.requests.AddCycleRequest
import org.flowalora.business.dtos.requests.UpdateCycleRequest
import org.flowalora.business.mappers.CycleMapper
import org.flowalora.dataAccess.CycleRepository
import org.koin.ktor.ext.inject

fun Route.cycleRoutes() {
    // Bağımlılık Enjeksiyonu (DI) manuel yapılıyor
//    val cycleRepository = CycleRepository()
//    val cycleMapper = CycleMapper()
// UseCase'leri İnşa Et
//    val addUseCase = AddCycleUseCase(cycleRepository, cycleMapper).decorate().withLogging().withTransaction().build()
//    val getListUseCase = GetCyclesByUserUseCase(cycleRepository, cycleMapper).decorate().withLogging().build()
//    val updateUseCase = UpdateCycleUseCase(cycleRepository, cycleMapper).decorate().withLogging().withTransaction().build()
//    val predictUseCase = PredictNextCycleUseCase(cycleRepository).decorate().withLogging().build()
//    val statsUseCase = GetCycleStatisticsUseCase(cycleRepository).decorate().withLogging().build()
//    val exportUseCase = ExportCyclesToCsvUseCase(cycleRepository).decorate().withLogging().build()

    val addCycleUseCase by inject<AddCycleUseCase>()
    val getListUseCase by inject<GetCyclesByUserUseCase>()
    val updateUseCase by inject<UpdateCycleUseCase>()
    val predictUseCase by inject<PredictNextCycleUseCase>()
    val statsUseCase by inject<GetCycleStatisticsUseCase>()
    val exportUseCase by inject<ExportCyclesToCsvUseCase>()
    route("/cycles") {

        // 1. Liste
        get("/my-history") {
            val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
            call.respond(getListUseCase.execute(userId))
        }

        // 2. Ekleme
        post("/add") {
            val request = call.receive<AddCycleRequest>()
            call.respond(HttpStatusCode.Created, addCycleUseCase.execute(request))
        }

        // 3. Güncelleme (PUT)
        put("/update") {
            val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
            val request = call.receive<UpdateCycleRequest>()
            // Pair kullanarak hem token'daki userId'yi hem de request'i gönderiyoruz
            val result = updateUseCase.execute(Pair(userId, request))
            call.respond(if (result.success) HttpStatusCode.OK else HttpStatusCode.BadRequest, result)
        }

        // 4. Analitik: Tahmin
        get("/predict") {
            val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
            call.respond(predictUseCase.execute(userId))
        }
        get("/stats") {
            val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
            call.respond(statsUseCase.execute(userId))
        }

        get("/export-csv") {
            val userId = call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong() ?: 0L
            val result = exportUseCase.execute(userId)

            if (result.success) {
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(
                        ContentDisposition.Parameters.FileName,
                        "flowalora_history.csv"
                    ).toString()
                )
                call.respondText(result.data ?: "", ContentType.Text.CSV)
            } else {
                call.respond(HttpStatusCode.BadRequest, result)
            }
        }
    }
}