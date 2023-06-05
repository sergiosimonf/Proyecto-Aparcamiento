package mappers

import dev.sergiosf.proyectoaparcamiento.dto.VehiculoDto
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo


fun List<Vehiculo>.toListDto() : List<VehiculoDto> {
    return map { it.toDto() }
}

fun Vehiculo.toDto() : VehiculoDto {
    return VehiculoDto(
        this.matricula,
        this.dniPropietario,
        this.marca,
        this.modelo,
        this.tipoVehiculo.value
    )
}

fun VehiculoDto.toVehiculo() : Vehiculo {
    return Vehiculo(
        this.matricula,
        this.dniPropietario,
        this.marca,
        this.modelo,
        Vehiculo.TipoVehiculo.valueOf(this.tipoVehiculo)
    )
}

fun List<VehiculoDto>.toListVehiculo() : List<Vehiculo> {
    return map { it.toVehiculo() }
}