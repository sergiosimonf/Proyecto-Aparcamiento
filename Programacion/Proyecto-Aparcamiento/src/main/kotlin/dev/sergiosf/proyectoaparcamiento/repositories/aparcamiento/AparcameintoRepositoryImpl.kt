package dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento

import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.service.database.DataBaseService
import java.time.LocalDateTime

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
                            fechaIngreso = LocalDateTime.parse(rs.getString("fechaIngreso")),
                            propietario = rs.getString("propietario")
                        )
                    )
                }
            }
        }
        return aparcados
    }

    override fun save(aparcamiento: Aparcamiento): Aparcamiento {
        val sql = "INSERT INTO aparcamiento (?, ?, ?, ?)"
        dataBaseService.db.use {
            it.prepareStatement(sql).use { stm ->
                stm.setString(1, aparcamiento.matricula)
                stm.setString(2, aparcamiento.tipoVehiculo.name)
                stm.setString(3, aparcamiento.fechaIngreso.toString())
                stm.setString(4, aparcamiento.propietario)
            }
        }
        return aparcamiento
    }
}