package dev.sergiosf.proyectoaparcamiento.service.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonWriter
import dev.sergiosf.proyectoaparcamiento.dto.exportJson
import dev.sergiosf.proyectoaparcamiento.dto.toDto
import dev.sergiosf.proyectoaparcamiento.errors.VehiculoError
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}


class StorageAparcameintoImpl : StorageAparcamiento {

    private val localFile =
        "export${File.separator}"


    override fun load() {
        TODO("Not yet implemented")
    }

    override fun save(file: File, vehiculos: List<Vehiculo>, profesor: List<Profesor>): Result<Long, VehiculoError> {
        return try {
            val exportJson = exportJson(vehiculos = vehiculos.toDto(), profesores = profesor.toDto())
//            println("Export ${exportJson.profesores.forEach { println(it) }}")
            val gson = GsonBuilder().registerTypeAdapter(exportJson::class.java, exportJson(vehiculos = vehiculos.toDto(), profesores = profesor.toDto())).setPrettyPrinting().create()
//            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(exportJson)
            file.writeText(jsonString)
            Ok(vehiculos.size.toLong())
        } catch (e: Exception) {
            Err(VehiculoError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }
    }
}