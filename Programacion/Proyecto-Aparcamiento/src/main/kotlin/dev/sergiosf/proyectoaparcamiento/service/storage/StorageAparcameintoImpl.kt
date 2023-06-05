package dev.sergiosf.proyectoaparcamiento.service.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.sergiosf.proyectoaparcamiento.dto.ExportBBDDJson
import dev.sergiosf.proyectoaparcamiento.dto.toListDto
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

            val gson = GsonBuilder().create()

            val exportBBDDJson = ExportBBDDJson(vehiculos = vehiculos.toListDto(), profesores = profesor.toListDto())

            val importType = object : TypeToken<ExportBBDDJson>() {}.type


            val jsonString = gson.toJson(exportBBDDJson, importType)

            file.writeText(jsonString)
            Ok(vehiculos.size.toLong())
        } catch (e: Exception) {
            Err(VehiculoError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }
    }
}