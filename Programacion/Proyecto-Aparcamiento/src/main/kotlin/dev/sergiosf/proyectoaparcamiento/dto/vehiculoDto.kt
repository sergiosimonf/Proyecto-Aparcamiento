package dev.sergiosf.proyectoaparcamiento.dto

import dev.sergiosf.proyectoaparcamiento.models.Vehiculo

class vehiculoDto (
    val matricula: String,
    val dniPropietario: String,
    val marca: String,
    val modelo: String,
    val tipoVehiculo: String
)


fun List<Vehiculo>.toDto() : List<vehiculoDto> {
    return map { it.toDto() }
}

fun Vehiculo.toDto() : vehiculoDto {
    return vehiculoDto(
        this.matricula,
        this.dniPropietario,
        this.marca,
        this.modelo,
        this.tipoVehiculo.value
    )
}

fun vehiculoDto.toVehiculo() : Vehiculo {
    return Vehiculo(
        this.matricula,
        this.dniPropietario,
        this.marca,
        this.modelo,
        Vehiculo.TipoVehiculo.valueOf(this.tipoVehiculo)
    )
}

fun List<vehiculoDto>.toVehiculo() : List<Vehiculo> {
    return map { it.toVehiculo() }
}