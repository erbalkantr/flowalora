package org.flowalora.di

import org.erbalkan.kernel.business.builders.decorate
import org.flowalora.business.concretes.AddCycleUseCase
import org.flowalora.business.concretes.ChangePasswordUseCase
import org.flowalora.business.concretes.ExportCyclesToCsvUseCase
import org.flowalora.business.concretes.GetCycleStatisticsUseCase
import org.flowalora.business.concretes.GetCyclesByUserUseCase
import org.flowalora.business.concretes.LoginUserUseCase
import org.flowalora.business.concretes.PredictNextCycleUseCase
import org.flowalora.business.concretes.RegisterUserUseCase
import org.flowalora.business.concretes.UpdateCycleUseCase
import org.flowalora.business.concretes.UpdateProfileUseCase
import org.flowalora.business.mappers.CycleMapper
import org.flowalora.business.mappers.UserMapper
import org.flowalora.dataAccess.CycleRepository
import org.flowalora.dataAccess.UserRepository
import org.koin.dsl.module

val appModule = module {
    // Repository'ler (Singleton olarak tanımlıyoruz)
    single { UserRepository() }
    single { CycleRepository() }

    // Mapper'lar
    single { UserMapper() }
    single { CycleMapper() }

    // UseCase'ler (Her çağrıldığında yeni bir tane oluşturulabilir - factory)
    factory { RegisterUserUseCase(get(), get())
        .decorate().withLogging().withTransaction()
        .withRule { request -> if(request.password.length < 8) "Şifre çok kısa !" else null }
        .build()
    }
    factory { LoginUserUseCase(get(), get())
        .decorate().withLogging().build()
    }
    factory { AddCycleUseCase(get(), get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { GetCyclesByUserUseCase(get(), get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { UpdateProfileUseCase(get(), get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { ChangePasswordUseCase(get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { ExportCyclesToCsvUseCase(get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { GetCycleStatisticsUseCase(get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { PredictNextCycleUseCase(get())
        .decorate().withLogging().withTransaction().build()
    }
    factory { UpdateCycleUseCase(get(), get())
        .decorate().withLogging().withTransaction().build()
    }
}