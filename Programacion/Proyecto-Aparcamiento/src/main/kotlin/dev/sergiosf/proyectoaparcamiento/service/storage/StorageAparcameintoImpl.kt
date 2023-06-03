package dev.sergiosf.proyectoaparcamiento.service.storage

import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}


class StorageAparcameintoImpl : StorageAparcamiento {

    private val localFile =
        "data${File.separator}export.json"


    override fun load() {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }
}