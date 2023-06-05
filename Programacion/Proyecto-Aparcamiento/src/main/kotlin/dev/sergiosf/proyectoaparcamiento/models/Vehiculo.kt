package dev.sergiosf.proyectoaparcamiento.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Vehiculo(
    @SerializedName("matricula")
    val matricula: String,
    @SerializedName("dniPropietario")
    val dniPropietario: String,
    @SerializedName("marca")
    val marca: String,
    @SerializedName("modelo")
    val modelo: String,
    @SerializedName("tipoVehiculo")
    val tipoVehiculo: TipoVehiculo
):Serializable {
    enum class TipoVehiculo(val value: String) {
        Combustion("Combustion"), Híbrido("Híbrido"), Eléctrico("Eléctrico"), NONE("")
    }
}

