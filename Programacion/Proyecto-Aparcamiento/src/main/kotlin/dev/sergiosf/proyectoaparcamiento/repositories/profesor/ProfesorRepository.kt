package dev.sergiosf.proyectoaparcamiento.repositories.profesor

import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.repositories.base.BaseRepository

interface ProfesorRepository : BaseRepository<Profesor> {
    fun findByDni(dni: String): Profesor?
    fun deleteByDni(dni: String)
    fun updateByDni(profesor: Profesor): Profesor
}