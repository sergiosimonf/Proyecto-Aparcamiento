package dev.sergiosf.proyectoaparcamiento.service.storage

import com.google.gson.*
import dev.sergiosf.proyectoaparcamiento.dto.ProfesorDto
import java.lang.reflect.Type


class ProfesorDtoAdapter : JsonSerializer<ProfesorDto>, JsonDeserializer<ProfesorDto> {
    override fun serialize(
        src: ProfesorDto,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("dni", src.dni)
        jsonObject.addProperty("nombre", src.nombre)
        jsonObject.addProperty("apellido", src.apellido)
        return jsonObject
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ProfesorDto {
        val jsonObject = json.asJsonObject
        val dni = jsonObject.get("dni").asString
        val nombre = jsonObject.get("nombre").asString
        val apellido = jsonObject.get("apellido").asString
        return ProfesorDto(dni, nombre, apellido)
    }
}

