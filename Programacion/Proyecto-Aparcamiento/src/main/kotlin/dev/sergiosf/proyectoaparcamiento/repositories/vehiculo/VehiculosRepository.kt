package dev.sergiosf.proyectoaparcamiento.repositories.vehiculo

import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.repositories.base.BaseRepository

interface VehiculosRepository : BaseRepository<Vehiculo> {
    fun findByMatricula(matricula: String): Vehiculo?
    fun deleteByMatricula(matricula: String)
}