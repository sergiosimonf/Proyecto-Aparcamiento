package dev.sergiosf.proyectoaparcamiento.dto

import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo

class profesorDto (
    val dni: String,
    val nombre: String,
    val apellido: String
)

fun List<Profesor>.toDto() : List<profesorDto> {
    return map { it.toDto() }
}

fun Profesor.toDto() : profesorDto {
    return profesorDto(
        this.dni,
        this.nombre,
        this.apellido
    )
}

fun profesorDto.toProfesor() : Profesor {
    return Profesor(
        this.dni,
        this.nombre,
        this.apellido
    )
}

fun List<profesorDto>.toVehiculo() : List<Profesor> {
    return map { it.toProfesor() }
}