package net.perfect.tea.lunatic

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.serialization.kotlinx.json.json
import java.io.File
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.* import io.ktor.server.application.*

object LunaticEngine {

    val mapper = YAMLMapper().registerKotlinModule()

    private val locales = mutableMapOf<String, Map<String, Any>>()

    fun loadLocales() {
        val folder = File("locales")
        if (!folder.exists()) {
            println("[LUNATIC LOCALES SYSTEM]: A Pasta de Locales não existe. Irei criar uma imediatamente! :3")
            folder.mkdirs()
            println("[LUNATIC LOCALES SYSTEM]: Pasta Criada!")
        }

        val files = folder.listFiles(){ _, name -> name.endsWith(".yml") }
        if (files.isNullOrEmpty()) {
            println("Nada encontrado na pasta locales!")
            return
        }

        files.forEach { file ->
            try {
                val content = mapper.readValue(file, Map::class.java)
                locales[file.nameWithoutExtension.lowercase()]
                println("Sucesso: Locale [${file.nameWithoutExtension}] carregado.")
            } catch (e: Exception) {
                println("Erro ao carregar ${file.name}: ${e.message}")
            }
        }
    }
    fun get(lang: String) = locales[lang.lowercase()] ?: locales["br"]

    fun main() {
        // Inicia o motor da LunaticEngine
        println("Iniciando LunaticEngine v1.0...")
        LunaticEngine.loadLocales()

        // === [ FRONTEND ] == //
        embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
            // Configurações (install) vêm aqui
            install(ContentNegotiation) {
                json()
            }

            // O routing deve estar aqui dentro
            routing {
                get("/lunatic/{lang}") {
                    val lang = call.parameters["lang"] ?: "br"
                    val data = LunaticEngine.get(lang)

                    if (data != null) {
                        call.respond(data)
                    }
                }
            }
        }.start(wait = true)
    }
}
