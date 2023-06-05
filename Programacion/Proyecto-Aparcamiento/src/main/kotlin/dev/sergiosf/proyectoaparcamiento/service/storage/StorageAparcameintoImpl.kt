package dev.sergiosf.proyectoaparcamiento.service.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dev.sergiosf.proyectoaparcamiento.dto.ExportBBDDJson
import dev.sergiosf.proyectoaparcamiento.dto.ProfesorDto
import dev.sergiosf.proyectoaparcamiento.dto.VehiculoDto

import dev.sergiosf.proyectoaparcamiento.errors.VehiculoError
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import mappers.toListDto
import mu.KotlinLogging
import java.io.File
import java.lang.reflect.Type

private val logger = KotlinLogging.logger {}


class StorageAparcameintoImpl : StorageAparcamiento {

    override fun save(file: File, vehiculos: List<Vehiculo>, profesor: List<Profesor>): Result<Long, VehiculoError> {
        return try {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ProfesorDto::class.java, ProfesorDtoAdapter())
                .registerTypeAdapter(VehiculoDto::class.java, VehiculoDtoAdapter())
                .registerTypeAdapter(ExportBBDDJson::class.java, ExportBBDDJsonAdapter())
                .create()

            val exportBBDDJson = ExportBBDDJson(profesor.toListDto(),vehiculos.toListDto(), )
            val type: Type = object : TypeToken<ExportBBDDJson>() {}.type

            val jsonString = gson.toJson(exportBBDDJson, type)
            file.writeText(jsonString)
            Ok(vehiculos.size.toLong())
        } catch (e: Exception) {
            Err(VehiculoError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }
    }
}

