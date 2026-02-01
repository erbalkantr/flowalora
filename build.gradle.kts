val ktor_version = "3.1.0"
val logback_version = "1.5.26"
val postgres_version = "42.7.7"
val exposed_version = "1.0.0"
val kernel_version = "2.0.0"
val koin_version = "4.0.0"
plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("io.ktor.plugin") version "3.1.0"
    application
}

group = "org.erbalkan"
version = "1.0.0"

application {
    // Paket adın org.flowalora ve dosya adın Application.kt ise sonuna 'Kt' eklemelisin
    mainClass.set("org.flowalora.ApplicationKt")
}

dependencies {
    testImplementation(kotlin("test"))
    // Kendi Kernel Kütüphanen
    implementation("com.github.erbalkantr:kernel:${kernel_version}")

    // KOIN IOC
    implementation("io.insert-koin:koin-ktor:${koin_version}")
    implementation("io.insert-koin:koin-logger-slf4j:${koin_version}")
    // Ktor Sunucu Bağımlılıkları
    implementation("io.ktor:ktor-server-status-pages:${ktor_version}")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // Veritabanı (Exposed & PostgreSQL)
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:6.2.1")
    // Loglama
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // JWT
    implementation("io.ktor:ktor-server-auth-jvm:${ktor_version}")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:${ktor_version}")
    // swagger
    implementation("io.ktor:ktor-server-swagger:${ktor_version}")
    implementation("io.ktor:ktor-server-openapi:${ktor_version}")

    // --- GÜVENLİK KISITLAMALARI (CRITICAL) ---
    constraints {
        implementation("ch.qos.logback:logback-core") {
            version { strictly(logback_version) }
        }
        implementation("org.postgresql:postgresql") {
            version { strictly(postgres_version) }
        }
    }
}
ktor {
    fatJar {
        archiveFileName.set("flowalora.jar") // Üretilecek dosyanın adını sabitleyelim
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

// OPT_İN
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}