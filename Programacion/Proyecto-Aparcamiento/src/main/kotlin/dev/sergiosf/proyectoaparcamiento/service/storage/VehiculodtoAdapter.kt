package dev.sergiosf.proyectoaparcamiento.service.storage

import com.google.gson.*
import dev.sergiosf.proyectoaparcamiento.dto.VehiculoDto
import java.lang.reflect.Type


class VehiculoDtoAdapter : JsonSerializer<VehiculoDto>, JsonDeserializer<VehiculoDto> {
    override fun serialize(
        src: VehiculoDto,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("matricula", src.matricula)
        jsonObject.addProperty("dniPropietario", src.dniPropietario)
        jsonObject.addProperty("marca", src.marca)
        jsonObject.addProperty("modelo", src.modelo)
        jsonObject.addProperty("tipoVehiculo", src.tipoVehiculo)
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): VehiculoDto {
        val jsonObject = json.asJsonObject
        val matricula = jsonObject.get("matricula").asString
        val dniPropietario = jsonObject.get("dniPropietario").asString
        val marca = jsonObject.get("marca").asString
        val modelo = jsonObject.get("modelo").asString
        val tipoVehiculo = jsonObject.get("tipoVehiculo").asString
        return VehiculoDto(matricula, dniPropietario, marca, modelo, tipoVehiculo)
    }
}