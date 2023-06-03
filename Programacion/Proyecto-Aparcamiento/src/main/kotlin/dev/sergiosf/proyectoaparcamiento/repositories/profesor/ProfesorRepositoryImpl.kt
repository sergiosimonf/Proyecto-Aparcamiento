package dev.sergiosf.proyectoaparcamiento.repositories.profesor

import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class ProfesorRepositoryImpl(
    private val dataBaseService: DataBaseService
) : ProfesorRepository {

//    init {
//        cargarEjemplos()
//    }

//    private fun cargarEjemplos() {
//        logger.debug { "Iniciando base de datos con algunos valores por defecto" }
//
//        val datosEjemplo = mutableListOf<Profesor>()
//
//        datosEjemplo.add(Profesor("53906421X", "Sergio", "Simón Fernández"))
//        datosEjemplo.add(Profesor("55555555B", "Samuel", "Sánchez Gutierrez"))
//        datosEjemplo.add(Profesor("99999999D", "Pepe", "Palotes García"))
//
//        saveAll(datosEjemplo)
//    }

    override fun findByDni(dni: String): Profesor? {
        logger.debug { "Buscando profesor con dni $dni" }
        val sql = "SELECT * FROM profesores WHERE dni = ?"
        var profesor: Profesor? = null
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, dni)
                val rs = stm.executeQuery()
                rs.let {
                    while (it.next()) {
                        profesor = Profesor(
                            dni = rs.getString("dni"),
                            nombre = rs.getString("nombre"),
                            apellido = rs.getString("apellido")
                        )
                    }
                }
            }
        }
        return profesor
    }

    override fun deleteByDni(dni: String) {
        logger.debug { "Eliminando profesor con dni $dni" }
        val sql = "DELETE FROM profesores WHERE dni = ?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, dni)

                stm.executeUpdate()
            }
        }
    }

    override fun findAll(): List<Profesor> {
        logger.debug { "Buscando profesores" }
        val sql = "SELECT * FROM profesores"
        var profesores = mutableListOf<Profesor>()
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    profesores.add(
                        Profesor(
                            dni = rs.getString("dni"),
                            nombre = rs.getString("nombre"),
                            apellido = rs.getString("apellido")
                        )
                    )
                }
            }
        }
        return profesores
    }

    override fun save(profesor: Profesor): Profesor {
        logger.debug { "Guardando profesor" }
        val sql = "INSERT INTO profesores VALUES(?, ?, ?)"
        dataBaseService.db.use {
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
}