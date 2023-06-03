package dev.sergiosf.proyectoaparcamiento.service.database

import dev.sergiosf.proyectoaparcamiento.config.AppConfig
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
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
        cargarEjemplos()
    }

    private fun cargarEjemplos() {
        cargarEjemplosVehiculos()
        cargarEjemplosProfesores()
    }

    private fun dropTables() {
        logger.debug { "Borrando las tablas de la base de datos" }

        val sql1 = "DROP TABLE IF EXISTS vehiculos"
        logger.debug { "Drop vehículos table" }
        db.use {
            it.createStatement().use { stmt ->
                stmt.executeUpdate(sql1)
            }
        }

        val sql2 = "DROP TABLE IF EXISTS profesores"
        logger.debug { "Drop profesores table" }
        db.use {
            it.createStatement().use { stmt ->
                stmt.executeUpdate(sql2)
            }
        }

        val sql3 = "DROP TABLE IF EXISTS aparcamiento"
        logger.debug { "Drop aparcamiento table" }
        db.use {
            it.createStatement().use { stmt ->
                stmt.executeUpdate(sql3)
            }
        }
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
                apellidos TEXT NOT NULL
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
                fechaIngreso STRING NOT NULL,
                propietario STRING NOT NULL
            )
        """.trimIndent()

        db.use {
            it.createStatement().use { stm ->
                stm.executeUpdate(sql3)
            }
        }
    }

    private fun cargarEjemplosVehiculos() {

        logger.debug { "Iniciando base de datos con algunos valores por defecto" }

        val datosEjemplo = mutableListOf<Vehiculo>()

        datosEjemplo.add(Vehiculo("1234ABC","53906421X", "Ford", "Mustang", Vehiculo.TipoVehiculo.COMBUSTION))
        datosEjemplo.add(Vehiculo("1234AAA","55555555B", "Honda", "Civic", Vehiculo.TipoVehiculo.HIBRIDO))
        datosEjemplo.add(Vehiculo("6969KFC","99999999D", "Volkswagen", "Golf", Vehiculo.TipoVehiculo.ELECTRICO))


        fun save(vehiculo: Vehiculo): Vehiculo {
            logger.debug { "Guardando vehículo $vehiculo" }
            val sql = "INSERT INTO vehiculos VALUES (?,?,?,?,?)"
            db.use {
                it.prepareStatement(sql).use { stm ->
                    stm.setString(1, vehiculo.matricula)
                    stm.setString(2, vehiculo.dniPropietario)
                    stm.setString(3, vehiculo.marca)
                    stm.setString(4, vehiculo.modelo)
                    stm.setString(5, vehiculo.tipoVehiculo.name)

                    stm.executeUpdate()
                }
            }
            return vehiculo
        }

        fun saveAll(vehiculos: List<Vehiculo>): List<Vehiculo> {
            logger.debug { "Guardando todas los vehiculos" }

            vehiculos.forEach {
                save(it)
            }
            return vehiculos
        }

        saveAll(datosEjemplo)
    }

    private fun cargarEjemplosProfesores() {
        logger.debug { "Iniciando base de datos con algunos valores por defecto" }

        val datosEjemplo = mutableListOf<Profesor>()

        datosEjemplo.add(Profesor("53906421X", "Sergio", "Simón Fernández"))
        datosEjemplo.add(Profesor("55555555B", "Samuel", "Sánchez Gutierrez"))
        datosEjemplo.add(Profesor("99999999D", "Pepe", "Palotes García"))


        fun save(profesor: Profesor): Profesor {
            logger.debug { "Guardando profesor" }
            val sql = "INSERT INTO profesores VALUES(?, ?, ?)"
            db.use {
                it.prepareStatement(sql).use { stm ->
                    stm.setString(1, profesor.dni)
                    stm.setString(2, profesor.nombre)
                    stm.setString(3, profesor.apellido)
                }
            }
            return profesor
        }

        fun saveAll(profesores : List<Profesor>): List<Profesor> {
            logger.debug { "Guardando todas los profesores" }

            profesores.forEach {
                save(it)
            }
            return profesores
        }


        saveAll(datosEjemplo)
    }
}