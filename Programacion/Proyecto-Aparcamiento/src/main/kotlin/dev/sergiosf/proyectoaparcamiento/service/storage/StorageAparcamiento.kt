package dev.sergiosf.proyectoaparcamiento.service.storage

import com.github.michaelbull.result.Result
import dev.sergiosf.proyectoaparcamiento.errors.VehiculoError
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import java.io.File


interface StorageAparcamiento {
    fun load()
    fun save(file: File, vehiculos: List<Vehiculo>, profesor: List<Profesor>): Result<Long, VehiculoError>
}