package dev.sergiosf.proyectoaparcamiento.controllers

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField

class GestionarController {
    @FXML
    lateinit var btnAgregar: Button

    @FXML
    lateinit var btnLimpiar: Button

    @FXML
    lateinit var btnCancelar: Button

    @FXML
    lateinit var textVehiculoMatriculaEdit: TextField

    @FXML
    lateinit var comboMatricula: ComboBox<Any>

    @FXML
    lateinit var textTipoVehiculo: TextField

    @FXML
    lateinit var textVehiculoModelo: TextField

    @FXML
    lateinit var textVehiculoMarca: TextField

    @FXML
    lateinit var textPropietarioApellido: TextField

    @FXML
    lateinit var textPropietarioNombre: TextField

    @FXML
    lateinit var textPropietarioDni: TextField
}