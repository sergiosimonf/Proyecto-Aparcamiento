package dev.sergiosf.proyectoaparcamiento.dto

import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo

data class exportJson(
    public val profesores: List<profesorDto>, public val vehiculos: List<vehiculoDto>
)