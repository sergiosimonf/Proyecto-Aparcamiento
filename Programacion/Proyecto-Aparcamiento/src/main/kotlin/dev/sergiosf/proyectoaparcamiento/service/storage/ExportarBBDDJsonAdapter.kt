package dev.sergiosf.proyectoaparcamiento.service.storage

import com.google.gson.*
import dev.sergiosf.proyectoaparcamiento.dto.ExportBBDDJson
import dev.sergiosf.proyectoaparcamiento.dto.ProfesorDto
import dev.sergiosf.proyectoaparcamiento.dto.VehiculoDto
import java.lang.reflect.Type


class ExportBBDDJsonAdapter : JsonSerializer<ExportBBDDJson>, JsonDeserializer<ExportBBDDJson> {
    override fun serialize(
        src: ExportBBDDJson,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        val profesoresArray = JsonArray()
        val vehiculosArray = JsonArray()

        src.profesores.forEach { profesor ->
            val profesorJson = context.serialize(profesor)
            profesoresArray.add(profesorJson)
        }

        src.vehiculos.forEach { vehiculo ->
            val vehiculoJson = context.serialize(vehiculo)
            vehiculosArray.add(vehiculoJson)
        }

        jsonObject.add("Profesores", profesoresArray)
        jsonObject.add("Vehículos", vehiculosArray)

        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ExportBBDDJson {
        val jsonObject = json.asJsonObject
        val profesoresArray = jsonObject.getAsJsonArray("Profesores")
        val vehiculosArray = jsonObject.getAsJsonArray("Vehículos")

        val profesores = mutableListOf<ProfesorDto>()
        val vehiculos = mutableListOf<VehiculoDto>()

        profesoresArray.forEach { profesorJson ->
            val profesor = context.deserialize<ProfesorDto>(profesorJson, ProfesorDto::class.java)
            profesores.add(profesor)
        }

        vehiculosArray.forEach { vehiculoJson ->
            val vehiculo = context.deserialize<VehiculoDto>(vehiculoJson, VehiculoDto::class.java)
            vehiculos.add(vehiculo)
        }

        return ExportBBDDJson(profesores, vehiculos)
    }
}