package dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento

import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.repositories.base.BaseRepository

interface AparcamientoRepository : BaseRepository<Aparcamiento> {
    fun deleteByMatricula(matricula: String)
}