package dev.sergiosf.proyectoaparcamiento.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Profesor(
    @SerializedName("dni")
    var dni: String,
    @SerializedName("nombre")
    var nombre: String,
    @SerializedName("apellido")
    var apellido: String
): Serializable