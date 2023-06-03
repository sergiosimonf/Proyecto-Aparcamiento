package dev.sergiosf.proyectoaparcamiento.config

import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import java.io.InputStream
import java.util.*


private val logger = KotlinLogging.logger {}

private const val CONFIG_FILE_NAME = "application.properties"

class AppConfig() : KoinComponent {

    val databaseUrl: String by lazy {
        readProperty("app.database.url") ?: "jdbc:sqlite:database.db"
    }


    private fun readProperty(propiedad: String): String? {
        logger.debug { "Leyendo propiedad: $propiedad" }
        val properties = Properties()
        val inputStream: InputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME)
            ?: throw Exception("No se puede leer el fichero de configuración $CONFIG_FILE_NAME")
        properties.load(inputStream)
        return properties.getProperty(propiedad)
    }
}