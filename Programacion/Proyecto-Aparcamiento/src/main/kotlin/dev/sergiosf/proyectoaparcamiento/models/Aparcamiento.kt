package dev.sergiosf.proyectoaparcamiento.models

import java.time.LocalDateTime

data class Aparcamiento(
    val matricula: String,
    val tipoVehiculo: Vehiculo.TipoVehiculo,
    val fechaIngreso: LocalDateTime = LocalDateTime.now(),
    val propietario: String
) {
    fun isElectrico(): Boolean = tipoVehiculo == Vehiculo.TipoVehiculo.ELECTRICO || tipoVehiculo == Vehiculo.TipoVehiculo.HIBRIDO
}