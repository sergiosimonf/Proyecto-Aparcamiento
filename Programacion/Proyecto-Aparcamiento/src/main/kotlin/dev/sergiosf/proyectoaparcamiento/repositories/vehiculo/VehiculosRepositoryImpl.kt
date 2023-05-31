package dev.sergiosf.proyectoaparcamiento.repositories.vehiculo

import dev.sergiosf.proyectoaparcamiento.models.TipoVehiculo
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class VehiculosRepositoryImpl(
    private val dataBaseService: DataBaseService
) : VehiculosRepository {
    override fun findByMatricula(matricula: String): Vehiculo? {
        logger.debug { "Buscando vehiculo con matricula $matricula" }
        var vehiculo: Vehiculo? = null
        val sql = "SELECT * FROM vehiculos WHERE matricula = ?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, matricula)
                val rs = stm.executeQuery()
                rs.let {
                    while (rs.next()) {
                        vehiculo = Vehiculo(
                            matricula = rs.getString("matricula"),
                            dniPropietario = rs.getString("dniPropietario"),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            tipoVehiculo = TipoVehiculo.valueOf(rs.getString("tipoVehiculo"))
                        )
                    }
                }
            }
        }
        return vehiculo
    }

    override fun deleteByMatricula(matricula: String) {
        logger.debug { "Borrando vehiculo con matricula $matricula" }
        var vehiculo: Vehiculo? = null
        val sql = "DELETE FROM vehiculos WHERE matricula = ?"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, matricula)

                stm.executeUpdate()
            }
        }
    }

    override fun findAll(): List<Vehiculo> {
        logger.debug { "Obteniendo todos vehículos de la base de datos" }
        val vehiculos = mutableListOf<Vehiculo>()
        val sql = "SELECT * FROM vehiculos"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                val rs = stm.executeQuery()
                while (rs.next()) {
                    vehiculos.add(
                        Vehiculo(
                            matricula = rs.getString("matricula"),
                            dniPropietario = rs.getString("dniPropietario"),
                            marca = rs.getString("marca"),
                            modelo = rs.getString("modelo"),
                            tipoVehiculo = TipoVehiculo.valueOf(rs.getString("tipoVehiculo"))
                        )
                    )
                }
            }
        }
        return vehiculos
    }

    override fun save(vehiculo: Vehiculo): Vehiculo {
        logger.debug { "Guardando vehículo $vehiculo" }
        val sql = "INSERT INTO vehiculos VALUES (?,?,?,?,?)"
        dataBaseService.db.use {
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
}