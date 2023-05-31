package dev.sergiosf.proyectoaparcamiento.service.database

import dev.sergiosf.proyectoaparcamiento.config.AppConfig
import mu.KotlinLogging
import java.sql.DriverManager

private val logger = KotlinLogging.logger {}

class DataBaseService(
    private val appConfig: AppConfig
) {

    val db
        get() = DriverManager.getConnection(appConfig.databaseUrl)

    init {
        logger.debug { "Iniciando databaseService" }
        dropTables()
        createTables()
    }

    private fun dropTables() {
        logger.debug { "Borrando las tablas de la base de datos" }

    }

    private fun createTables() {
        logger.debug { "Creando las tablas de la base de datos" }

        val sql1 = """
            CREATE TABLE IF NOT EXISTS vehiculos (
                matricula STRING NOT NULL PRIMARY KEY,
		        dniPropietario TEXT NOT NULL,
                marca TEXT NOT NULL,
                modelo TEXT NOT NULL,
                tipoVehiculo TEXT NOT NULL
            )
        """.trimIndent()

        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql1)
            }
        }

        val sql2 = """
            CREATE TABLE IF NOT EXISTS profesores (
                dni TEXT NOT NULL PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellidos TEXT NOT NULL,
            )
        """.trimIndent()

        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql2)
            }
        }

        val sql3 = """
            CREATE TABLE IF NOT EXISTS aparcamiento (
                matricula STRING NOT NULL PRIMARY KEY,
                tipoVehiculo TEXT NOT NULL,
                fechaIngreso STRING NOT NULL
            )
        """.trimIndent()

        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql3)
            }
        }
    }
}