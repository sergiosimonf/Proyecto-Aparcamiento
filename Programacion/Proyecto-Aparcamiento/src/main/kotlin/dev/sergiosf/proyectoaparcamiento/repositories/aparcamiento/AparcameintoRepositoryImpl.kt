package dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento

import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AparcameintoRepositoryImpl(
    private val dataBaseService: DataBaseService
) : AparcamientoRepository {
    override fun deleteByMatricula(matricula: String) {
        val sql = "DELETE FROM aparcamiento WHERE matricula = ?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, matricula)

                stm.executeUpdate()
            }
        }
    }

    override fun findAll(): List<Aparcamiento> {
        val sql = "SELECT * FROM aparcamiento"
        val aparcados = mutableListOf<Aparcamiento>()
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    aparcados.add(
                        Aparcamiento(
                            matricula = rs.getString("matricula"),
                            tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(rs.getString("tipoVehiculo")),
                            fechaIngreso = rs.getString("fechaIngreso"),
                            propietario = rs.getString("propietario")
                        )
                    )
                }
            }
        }
        return aparcados
    }

    override fun findByMatricula(matricula: String): Aparcamiento? {
        val sql = "SELECT * FROM aparcamiento WHERE matricula = ?"
        var aparcado: Aparcamiento? = null
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, matricula)
                val rs = stm.executeQuery()
                rs.let {
                    while (rs.next()) {
                        aparcado = Aparcamiento(
                            matricula = rs.getString("matricula"),
                            tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(rs.getString("tipoVehiculo")),
                            fechaIngreso = rs.getString("fechaIngreso"),
                            propietario = rs.getString("propietario")
                        )
                    }
                }
            }
        }
        return aparcado
    }

    override fun save(aparcamiento: Aparcamiento): Aparcamiento {
        logger.info("Aparcando un vehiculo $aparcamiento")
        val sql = "INSERT INTO aparcamiento VALUES (?, ?, ?, ?)"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, aparcamiento.matricula)
                stm.setString(2, aparcamiento.tipoVehiculo.name)
                stm.setString(3, aparcamiento.fechaIngreso.toString())
                stm.setString(4, aparcamiento.propietario)

                stm.executeUpdate()
            }
        }
        return aparcamiento
    }

    override fun saveAll(entity: List<Aparcamiento>): List<Aparcamiento> {
        logger.debug { "Guardando todas los vehículos aparcados" }

        entity.forEach {
            save(it)
        }
        return entity
    }
}