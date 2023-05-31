package dev.sergiosf.proyectoaparcamiento.models

data class Aparcamiento(
    val matricula: String,
    val tipoVehiculo: TipoVehiculo,
    val fechaIngreso: String
)