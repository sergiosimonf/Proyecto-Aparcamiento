package dev.sergiosf.proyectoaparcamiento.routes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import java.io.InputStream
import java.net.URL
import java.util.*


private val logger = KotlinLogging.logger {}

const val ICON_APP = "images/car-icon.png"

object RoutesManager {

    private lateinit var mainStage: Stage
    private lateinit var _activeStage: Stage

    init {
        Locale.setDefault(Locale("es", "ES"))
    }

    val activeStage: Stage
        get() = _activeStage

    lateinit var app: Application

    enum class View(val fxml: String) {
        MAIN("views/main-view.fxml"),
        ABOUT("views/about-view.fxml"),
        AGREGAR("views/agregar-view.fxml"),
        GESTIONAR("views/gestionar-view.fxml"),
        APARCAR("views/aparcar-view.fxml")
    }

    fun initMainStage(stage: Stage) {
        logger.debug { "Iniciando la vista principal" }

        val fxml = FXMLLoader(getResource(View.MAIN.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 694.0, 517.0)
        stage.title = "Concesionario"
        stage.icons.add(Image(getResourceAsStream(ICON_APP)))
        stage.setOnCloseRequest {
            Alert(Alert.AlertType.WARNING)
                .apply {
                    title = "Atención"
                    headerText = "Se va a cerrar la aplicación"
                    contentText = "Seguro que quieres cerrar la aplicación"
                }.showAndWait()
        }
        stage.scene = scene
        mainStage = stage
        _activeStage = stage
        mainStage.show()
    }

    fun initAparcarStage() {
        logger.debug { "Iniciando la vista 'Aparcar'" }

        val fxml = FXMLLoader(this.getResource(View.APARCAR.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 341.0, 489.0)
        val stage = Stage()
        stage.title = "Aparcar un vehiculo"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream(ICON_APP)))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }

    fun initGestionarStage() {
        logger.debug { "Iniciando la vista 'Gestionar'" }

        val fxml = FXMLLoader(this.getResource(View.GESTIONAR.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 341.0, 489.0)
        val stage = Stage()
        stage.title = "Gestionar datos"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream(ICON_APP)))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }

    fun initAgregarStage() {
        logger.debug { "Iniciando la vista 'Agregar'" }

        val fxml = FXMLLoader(this.getResource(View.AGREGAR.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 319.0, 460.0)
        val stage = Stage()
        stage.title = "Agregar datos"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream(ICON_APP)))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }

    fun initAboutStage() {
        logger.debug { "Iniciando la vista 'Acerca de'" }

        val fxml = FXMLLoader(this.getResource(View.ABOUT.fxml))
        val parentRoot = fxml.load<Pane>()
        val scene = Scene(parentRoot, 329.0, 155.0)
        val stage = Stage()
        stage.title = "Concesionario"
        stage.isResizable = false
        stage.icons.add(Image(this.getResourceAsStream(ICON_APP)))
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)

        stage.isResizable = false
        _activeStage = stage
        stage.show()
    }

    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }

    fun getResourceAsStream(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
    }
}