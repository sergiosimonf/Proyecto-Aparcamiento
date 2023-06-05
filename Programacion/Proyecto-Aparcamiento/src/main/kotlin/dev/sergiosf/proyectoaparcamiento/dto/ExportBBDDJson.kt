package dev.sergiosf.proyectoaparcamiento.dto

import com.google.gson.annotations.SerializedName

data class ExportBBDDJson(
    @SerializedName("Profesores")
    val profesores: List<ProfesorDto>,
    @SerializedName("Vehículos")
    val vehiculos: List<VehiculoDto>
)