package dev.sergiosf.proyectoaparcamiento.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label

class AparcamientoController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
}