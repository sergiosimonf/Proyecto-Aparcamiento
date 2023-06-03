package dev.sergiosf.proyectoaparcamiento.models

import java.time.LocalDateTime

data class Aparcamiento(
    val matricula: String,
    val tipoVehiculo: Vehiculo.TipoVehiculo,
    val fechaIngreso: String = LocalDateTime.now().toString(),
    val propietario: String
) {
    fun isElectrico(): Boolean = tipoVehiculo == Vehiculo.TipoVehiculo.Eléctrico || tipoVehiculo == Vehiculo.TipoVehiculo.Híbrido
}