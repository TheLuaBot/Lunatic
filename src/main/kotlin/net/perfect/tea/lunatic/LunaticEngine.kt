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
import org.yaml.snakeyaml.Yaml
import java.io.File

object LunaticEngine {
    val mapper = YAMLMapper().registerKotlinModule()
    // Guardamos como Map<String, Any> para o Ktor conseguir converter para JSON
    private val locales = mutableMapOf<String, Any>()



    fun loadLocales() {
        val rootFolder = File("locales")
        if (!rootFolder.exists() || !rootFolder.isDirectory) {
            println("❌ Pasta 'locales' não encontrada!")
            return
        }

        // Busca todos os arquivos .yml em qualquer subpasta
        val yamlFiles = rootFolder.walkTopDown().filter { it.extension == "yml" || it.extension == "yaml" }.toList()

        if (yamlFiles.isEmpty()) {
            println("⚠️ Nenhum arquivo .yml encontrado nas subpastas de 'locales'!")
            return
        }

        yamlFiles.forEach { file ->
            // Pega o nome da pasta pai como o nome do idioma (ex: "br")
            val lang = file.parentFile.name
            try {
                val content: Map<String, Any> = Yaml().load(file.inputStream())
                locales[lang] = content
                println("✅ Locale [$lang] carregado do arquivo: ${file.path}")
            } catch (e: Exception) {
                println("❌ Erro ao carregar $lang: ${e.message}")
            }
        }
    }

    fun get(lang: String) = locales[lang.lowercase()] ?: locales["br"]
}


fun main() {

    // Inicia a LunaticEngine(Motor)
    println("==================================")
    println("🌙 Iniciando LunaticEngine v1.0...")
    println("==================================")
    LunaticEngine.loadLocales()



    val port = System.getenv("PORT")?.toInt() ?: 8080

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
