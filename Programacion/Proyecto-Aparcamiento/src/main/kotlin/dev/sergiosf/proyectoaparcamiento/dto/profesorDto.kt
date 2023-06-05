package dev.sergiosf.proyectoaparcamiento.dto

import com.google.gson.annotations.SerializedName
import dev.sergiosf.proyectoaparcamiento.models.Profesor

open class ProfesorDto (
    @SerializedName("dni")
    var dni: String,
    @SerializedName("nombre")
    var nombre: String,
    @SerializedName("apellido")
    var apellido: String
)

fun List<Profesor>.toListDto() : List<ProfesorDto> {
    return map { it.toDto() }
}

fun Profesor.toDto() : ProfesorDto {
    return ProfesorDto(
        this.dni,
        this.nombre,
        this.apellido
    )
}

fun ProfesorDto.toProfesor() : Profesor {
    return Profesor(
        this.dni,
        this.nombre,
        this.apellido
    )
}

fun List<ProfesorDto>.toListVehiculo() : List<Profesor> {
    return map { it.toProfesor() }
}