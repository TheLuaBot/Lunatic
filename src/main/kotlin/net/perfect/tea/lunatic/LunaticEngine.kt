package net.perfect.tea.lunatic

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

object LunaticEngine {

    val mapper = YAMLMapper().registerKotlinModule()

    private val locales = mutableMapOf<String, Any>()

    fun loadLocales() {
        val folder = File("locales")
        if (!folder.exists()) {
            println("[LUNATIC LOCALES SYSTEM]: A Pasta de Locales não existe. Irei criar uma imediatamente! :3")
            folder.mkdirs()
            println("[LUNATIC LOCALES SYSTEM]: Pasta Criada!")
        }

        val files = folder.listFiles { _, name -> name.endsWith(".yml") }
        if (files.isNullOrEmpty()) {
            println("Nada encontrado na pasta locales!")
            return
        }

        files.forEach { file ->
            try {
                val content = mapper.readValue(file, Map::class.java)
                // Atribuindo o conteúdo lido ao mapa de locales
                locales[file.nameWithoutExtension.lowercase()] = content
                println("Sucesso: Locale [${file.nameWithoutExtension}] carregado.")
            } catch (e: Exception) {
                println("Erro ao carregar ${file.name}: ${e.message}")
            }
        }
    }

    fun get(lang: String) = locales[lang.lowercase()] ?: locales["br"]
}

fun main() {
    // Inicia o motor da LunaticEngine
    println("Iniciando LunaticEngine v1.0...")
    LunaticEngine.loadLocales()

    val port = System.getenv("PORT")?.toInt() ?: 8080

    // === [ FRONTEND ] == //
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/lunatic/{lang}") {
                val lang = call.parameters["lang"] ?: "br"
                val data = LunaticEngine.get(lang)

                if (data != null) {
                    call.respond(data)
                } else {
                    call.respond(io.ktor.http.HttpStatusCode.NotFound, mapOf("error" to "Locale not found"))
                }
            }
        }
    }.start(wait = true)
}
