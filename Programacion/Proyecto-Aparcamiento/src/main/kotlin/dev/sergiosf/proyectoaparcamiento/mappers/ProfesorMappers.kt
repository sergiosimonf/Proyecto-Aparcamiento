package mappers

import dev.sergiosf.proyectoaparcamiento.dto.ProfesorDto
import dev.sergiosf.proyectoaparcamiento.models.Profesor


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