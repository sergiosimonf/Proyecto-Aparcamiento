package dev.sergiosf.proyectoaparcamiento.dto

import com.google.gson.annotations.SerializedName
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo

data class VehiculoDto (
    @SerializedName("matricula")
    var matricula: String,
    @SerializedName("dniPropietario")
    var dniPropietario: String,
    @SerializedName("marca")
    var marca: String,
    @SerializedName("modelo")
    var modelo: String,
    @SerializedName("tipoVehiculo")
    var tipoVehiculo: String
)


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