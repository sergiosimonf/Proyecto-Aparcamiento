package dev.sergiosf.proyectoaparcamiento.controllers

import com.github.michaelbull.result.*
import dev.sergiosf.proyectoaparcamiento.errors.PersonaError
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.viewmodels.AparcamientoViewModels
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {}

class AgregarController : KoinComponent {
    @FXML
    lateinit var btnAgregar: Button

    @FXML
    lateinit var btnLimpiar: Button

    @FXML
    lateinit var btnCancelar: Button

    @FXML
    lateinit var textVehiculoModelo: TextField

    @FXML
    lateinit var textVehiculoMarca: TextField

    @FXML
    lateinit var textVehiculoMatricula: TextField

    @FXML
    lateinit var comboTipoVehiculo: ComboBox<String>

    @FXML
    lateinit var textPropietarioApellido: TextField

    @FXML
    lateinit var textPropietarioNombre: TextField

    @FXML
    lateinit var textPropietarioDni: TextField

    private val viewModel: AparcamientoViewModels by inject()

    fun initialize() {

        // Iniciamos los bindings
        initBindings()
        // Iniciamos los eventos
        initEventos()
    }

    private fun initBindings() {
        comboTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.typeMotor)
        comboTipoVehiculo.selectionModel.selectFirst()
    }

    private fun initEventos() {
        btnAgregar.setOnAction {
            onGuardarAction()
        }
        btnLimpiar.setOnAction {
            onLimpiarAction()
        }
        btnCancelar.setOnAction {
            onCancelarAction()
        }
    }

    private fun onGuardarAction() {
        logger.debug { "onGuardarAction" }
        validateForm().onSuccess {
            if (!viewModel.isDniRegistrado(dni = it.dni)) {
                if (!viewModel.isMatriculaRegistrado(it.matricula)) {
                    viewModel.guardar(it)
                    cerrarVentana()
                } else {
                    Alert(Alert.AlertType.ERROR).apply {
                        title = "Error al guardar "
                        headerText = "Se ha producido un error al guardar "
                        contentText = "La matricula ya esta registrada"
                    }.showAndWait()
                }
            } else {
                Alert(Alert.AlertType.ERROR).apply {
                    title = "Error al guardar"
                    headerText = "Se ha producido un error al guardar"
                    contentText = "El dni ya esta registrado"
                }.showAndWait()
            }
        }.onFailure { e ->
            Alert(Alert.AlertType.ERROR).apply {
                title = "Error al guardar"
                headerText = "Se ha producido un error al guardar"
                contentText = e.message
            }.showAndWait()
        }
    }

    private fun validateForm(): Result<AparcamientoViewModels.VehiculoYPropietarioFormulario, PersonaError> {
        logger.debug { "validateForm" }

        val dniRegex = "^[0-9]{8,8}[A-Za-z]".toRegex()

        if (textPropietarioNombre.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El nombre no puede estar vacio"))
        }

        if (textPropietarioApellido.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El apellido no puede estar vacio"))
        }

        if (!dniRegex.matches(textPropietarioDni.text)) {
            return Err(PersonaError.ValidateProblem("El dni esta mal formado"))
        }

        val matriculaRegex = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}".toRegex()

        if (!matriculaRegex.matches(textVehiculoMatricula.text)) {
            return Err(PersonaError.ValidateProblem("La matricula esta mal formada"))
        }

        if (textVehiculoMarca.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("La marca no ha sido introducida"))
        }

        if (textVehiculoModelo.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El modelo no ha sido introducido"))
        }

        if (comboTipoVehiculo.value == Vehiculo.TipoVehiculo.NONE.value) {
            return Err(PersonaError.ValidateProblem("El tipo vehiculo no exite o no es valido"))
        }

        return Ok(
            AparcamientoViewModels.VehiculoYPropietarioFormulario(
                dni = textPropietarioDni.text,
                nombre = textPropietarioNombre.text,
                apellido = textPropietarioApellido.text,
                matricula = textVehiculoMatricula.text,
                marca = textVehiculoMarca.text,
                modelo = textVehiculoModelo.text,
                tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(comboTipoVehiculo.value),
            )
        )
    }

    private fun onLimpiarAction() {
        logger.debug { "onLimpiarAction" }
        limpiarForm()
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        cerrarVentana()
    }

    private fun limpiarForm() {
        // Limpiamos el estado actual
        textPropietarioDni.text = ""
        textPropietarioNombre.text = ""
        textPropietarioApellido.text = ""
        comboTipoVehiculo.value = Vehiculo.TipoVehiculo.NONE.value
        textVehiculoMarca.text = ""
        textVehiculoModelo.text = ""
        textVehiculoMatricula.text = ""
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }

}