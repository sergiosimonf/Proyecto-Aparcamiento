package dev.sergiosf.proyectoaparcamiento.repositories.profesor

import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class ProfesorRepositoryImpl(
    private val dataBaseService: DataBaseService
) : ProfesorRepository {

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
}