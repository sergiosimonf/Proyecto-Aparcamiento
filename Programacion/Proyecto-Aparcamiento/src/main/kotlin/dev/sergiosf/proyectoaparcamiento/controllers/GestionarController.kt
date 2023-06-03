package dev.sergiosf.proyectoaparcamiento.controllers

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onSuccess
import dev.sergiosf.proyectoaparcamiento.errors.PersonaError
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.viewmodels.AparcamientoViewModels
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = KotlinLogging.logger {}

class GestionarController: KoinComponent {

    @FXML
    lateinit var comboTipoVehiculo: ComboBox<String>

    @FXML
    lateinit var btnEliminar: Button

    @FXML
    lateinit var btnEditar: Button

    @FXML
    lateinit var btnCancelar: Button

    @FXML
    lateinit var comboMatricula: ComboBox<String>

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

    private val viewModel: AparcamientoViewModels by inject()

    @FXML
    fun initialize() {
        logger.debug { "Inicializando ExpedientesDeViewController FXML" }

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initEventos() {
        btnCancelar.setOnAction {
            onCancelarAction()
        }

        btnEliminar.setOnAction {
            onEliminarAction()
        }

        btnEditar.setOnAction {
            onEditarAction()
        }
    }

    private fun onEditarAction() {
        if (comboMatricula.value.isEmpty()){
            return
        }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Atención"
            headerText = "¿Desea editar el usuario y vehiculo?"
            contentText = "¡Esta acción no se puede deshacer!. Va a editar el usuario y vehiculo asociado con la siguiente matricula ${comboMatricula.value}"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                validateForm().onSuccess {
                    viewModel.editarVehiculoPropietario(it)
                }
            }
        }
    }

    private fun validateForm(): Result<AparcamientoViewModels.VehiculoYPropietarioFormulario, PersonaError> {
        logger.debug { "validateForm" }

        val dniRegex = "^[0-9]{8,8}[A-Za-z]\$\n".toRegex()
        val stringRegex = "[a-zA-Z]+\\d*".toRegex()

        if (textPropietarioNombre.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El nombre no puede estar vacio"))
        }

        if (textPropietarioApellido.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El apellido no puede estar vacio"))
        }

        if (!dniRegex.matches(textPropietarioDni.text)) {
            return Err(PersonaError.ValidateProblem("El dni esta mal formado"))
        }

        if (stringRegex.matches(textPropietarioNombre.text)) {
            return Err(PersonaError.ValidateProblem("El nombre esta mal formado"))
        }

        if (!stringRegex.matches(textPropietarioApellido.text)) {
            return Err(PersonaError.ValidateProblem("El apellido esta mal formado"))
        }

        val matriculaRegex = "^[0-9]{1,4}(?!.*(LL|CH))[BCDFGHJKLMNPRSTVWXYZ]{3}".toRegex()

        if (!matriculaRegex.matches(comboMatricula.value)) {
            return Err(PersonaError.ValidateProblem("La matricula esta mal formada"))
        }

        if (textVehiculoMarca.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("La marca no ha sido introducida"))
        }

        if (textVehiculoModelo.text.isEmpty()) {
            return Err(PersonaError.ParametroNoIntroducido("El modelo no ha sido introducido"))
        }

        if (Vehiculo.TipoVehiculo.valueOf(comboTipoVehiculo.value) != Vehiculo.TipoVehiculo.Combustion || Vehiculo.TipoVehiculo.valueOf(
                comboTipoVehiculo.value
            ) != Vehiculo.TipoVehiculo.Eléctrico || Vehiculo.TipoVehiculo.valueOf(comboTipoVehiculo.value) != Vehiculo.TipoVehiculo.Híbrido
        ) {
            return Err(PersonaError.ValidateProblem("El tipo vehiculo no exite o no es valido"))
        }

        return Ok(
            AparcamientoViewModels.VehiculoYPropietarioFormulario(
                dni = textPropietarioDni.text,
                nombre = textPropietarioNombre.text,
                apellido = textPropietarioApellido.text,
                matricula = comboMatricula.value,
                marca = textVehiculoMarca.text,
                modelo = textVehiculoModelo.text,
                tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(comboTipoVehiculo.value),
            )
        )
    }

    private fun onEliminarAction() {
        eliminarVehiculoPropietario()
    }

    private fun eliminarVehiculoPropietario() {
        if (comboMatricula.value.isEmpty()){
            return
        }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Atención"
            headerText = "¿Desea eliminar el usuario y vehiculo?"
            contentText = "¡Esta acción no se puede deshacer!. Va a eliminar el usuario y vehiculo asociado con la siguiente matricula ${comboMatricula.value}"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                viewModel.deleteVehiculoPropietario(comboMatricula.value)
            }
        }
    }

    private fun initBindings() {
        // ComboBox Matricula
        comboMatricula.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos.map { it.matricula })
        comboMatricula.selectionModel.selectFirst()

        comboTipoVehiculo.items = FXCollections.observableArrayList(viewModel.state.value.typeMotor)
        comboTipoVehiculo.selectionModel.selectFirst()
    }

    private fun onCancelarAction() {
        logger.debug { "onCancelarAction" }
        cerrarVentana()
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }
}