package dev.sergiosf.proyectoaparcamiento.controllers

import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.viewmodels.AparcamientoViewModels
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private val logger = KotlinLogging.logger {}

class AparcarController : KoinComponent {
    @FXML
    lateinit var btnAgregar: Button

    @FXML
    lateinit var btnCancelar: Button

    @FXML
    lateinit var comboMatricula: ComboBox<String>

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

    private val viewModel: AparcamientoViewModels by inject()

    @FXML
    fun initialize() {
        logger.debug { "Inicializando la vista para aparcar un vehiculo" }

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initBindings() {
        logger.debug { "Iniciamos los bindings" }

        // ComboBox Matricula
        comboMatricula.items = FXCollections.observableArrayList(viewModel.state.value.vehiculos.map { it.matricula })
        comboMatricula.selectionModel.selectFirst()
    }


    private fun initEventos() {
        comboMatricula.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        btnAgregar.setOnAction {
            onAparcarAction()
        }

        btnCancelar.setOnAction {
            onCancelarAction()
        }
    }

    private fun onCancelarAction() {
        cerrarVentana()
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        btnCancelar.scene.window.hide()
    }

    private fun onAparcarAction() {
        logger.debug { "Aparcando un vehiculo ${textTipoVehiculo.text}" }

        if (comboMatricula.value.isEmpty()) {
            return
        }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Ocupar la plaza"
            headerText = "¿Desea ocupar la plaza actual?"
            contentText = "Esta acción no se puede deshacer y no permitirá a otros ocupar su puesto."
        }.showAndWait().ifPresent { buttonType ->
            val aparcamiento = recogerDatosFormulario()
            when (buttonType == ButtonType.OK) {
                !viewModel.isAparcadoByMatricula(comboMatricula.value) -> showAlertOperacion(alerta = Alert.AlertType.ERROR, title = "Vehiculo ya aparcado", header = "El vehiculo ya esta en el aparcamiento", mensaje = "El vehiculo ya esta aparcado. No se puede aparcar dos veces el mismo vehiculo")
                (viewModel.state.value.numCapacidad > 10) -> showAlertOperacion(alerta = Alert.AlertType.ERROR, title = "No caben más vehículos", header = "Se ha alcanzado el número máximo de vehículos", mensaje = "No se pueden introducir más vehículos en el sistema debido a que esta esta completo")
                ((aparcamiento.tipoVehiculo.value == Vehiculo.TipoVehiculo.Eléctrico.value) && (viewModel.state.value.numVehiculosElectricos > 2)) -> showAlertOperacion(alerta = Alert.AlertType.ERROR, title = "No caben más vehículos eléctricos", header = "Se ha alcanzado el número máximo de vehículos eléctricos", mensaje = "Ya hay 2 vehículos eléctricos ocupando la plaza reservada para este tipo de vehículo")
                else -> {
                    viewModel.ocuparPlaza(aparcamiento)
                    cerrarVentana()
                }
            }
        }
    }

    private fun showAlertOperacion(
        alerta: Alert.AlertType = Alert.AlertType.ERROR,
        title: String = "",
        header: String = "",
        mensaje: String = ""
    ) {
        Alert(alerta).apply {
            this.title = title
            this.headerText = header
            this.contentText = mensaje
        }.showAndWait()
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected $newValue" }
        loadForumualrioData(newValue)
    }

    private fun recogerDatosFormulario(): Aparcamiento {
        return Aparcamiento(
            matricula = comboMatricula.value,
            propietario = textPropietarioNombre.text + " ${textPropietarioApellido.text}",
            tipoVehiculo = Vehiculo.TipoVehiculo.valueOf(textTipoVehiculo.text)
        )
    }

    private fun loadForumualrioData(newValue: String) {

        val vehiculo = viewModel.state.value.vehiculos.filter { it.matricula == newValue }.first()
        val propietario = viewModel.state.value.profesor.filter { it.dni == vehiculo.dniPropietario }.first()

        textPropietarioDni.text = propietario.dni
        textPropietarioNombre.text = propietario.nombre
        textPropietarioApellido.text = propietario.apellido
        textVehiculoMarca.text = vehiculo.marca
        textVehiculoModelo.text = vehiculo.modelo
        textTipoVehiculo.text = vehiculo.tipoVehiculo.value
    }
}