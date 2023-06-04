package dev.sergiosf.proyectoaparcamiento.controllers

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.routes.RoutesManager
import dev.sergiosf.proyectoaparcamiento.viewmodels.AparcamientoViewModels
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import mu.KotlinLogging
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

private val logger = KotlinLogging.logger {}

class AparcamientoController : KoinComponent {

    @FXML
    lateinit var menuImportar: MenuItem

    @FXML
    lateinit var menuExportar: MenuItem

    @FXML
    lateinit var menuAcercaDe: MenuItem

    @FXML
    lateinit var menuSalir: MenuItem

    @FXML
    lateinit var textTipoVehiculo: TextField

    @FXML
    lateinit var textVehiculoModelo: TextField

    @FXML
    lateinit var textVehiculoMarca: TextField

    @FXML
    lateinit var textVehiculoMatricula: TextField

    @FXML
    lateinit var textPropietarioApellido: TextField

    @FXML
    lateinit var textPropietarioNombre: TextField

    @FXML
    lateinit var textPropietarioDni: TextField

    @FXML
    lateinit var btnGestionar: Button

    @FXML
    lateinit var btnLiberar: Button

    @FXML
    lateinit var btnAparcar: Button

    @FXML
    lateinit var btnAgregar: Button

    @FXML
    lateinit var tableColumnFechaIngreso: TableColumn<Aparcamiento, String>

    @FXML
    lateinit var tableColumPropietario: TableColumn<Aparcamiento, String>

    @FXML
    lateinit var tableColumnMatricula: TableColumn<Aparcamiento, String>

    @FXML
    lateinit var tableVehiculosAparcados: TableView<Aparcamiento>

    @FXML
    lateinit var comboTipoFiltro: ComboBox<String>

    @FXML
    lateinit var textBuscador: TextField

    private val viewModel: AparcamientoViewModels by inject()

    @FXML
    fun initialize() {
        logger.debug { "Inicializando ExpedientesDeViewController FXML" }

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initBindings() {
        logger.debug { "Inicializando bindings" }

        // Filtro de motor
        comboTipoFiltro.items = FXCollections.observableArrayList(viewModel.state.value.typeVehiculo)
        comboTipoFiltro.selectionModel.selectFirst()

        // Tabla de vehiculos aparcados
        tableVehiculosAparcados.items = FXCollections.observableArrayList(viewModel.state.value.listaAparcados)
        tableVehiculosAparcados.selectionModel.selectionMode = SelectionMode.SINGLE

        // Valores de las columnas
        tableColumnMatricula.cellValueFactory = PropertyValueFactory("matricula")
        tableColumPropietario.cellValueFactory = PropertyValueFactory("propietario")
        tableColumnFechaIngreso.cellValueFactory = PropertyValueFactory("fechaIngreso")

        viewModel.state.addListener { _, oldState, newState ->
            updatesFormulario(oldState, newState)
            updatesTabla()
        }

    }

    private fun updatesFormulario(
        oldState: AparcamientoViewModels.AparcamientoState,
        newState: AparcamientoViewModels.AparcamientoState
    ) {
        if (oldState.vehiculoSeleccionado != newState.vehiculoSeleccionado) {
            textPropietarioDni.text = newState.vehiculoSeleccionado.dni
            textPropietarioNombre.text = newState.vehiculoSeleccionado.nombre
            textPropietarioApellido.text = newState.vehiculoSeleccionado.apellido
            textVehiculoMatricula.text = newState.vehiculoSeleccionado.matricula
            textVehiculoMarca.text = newState.vehiculoSeleccionado.marca
            textVehiculoModelo.text = newState.vehiculoSeleccionado.modelo
            textTipoVehiculo.text = newState.vehiculoSeleccionado.tipoVehiculo.value
        }
    }

    private fun updatesTabla(
    ) {
        tableVehiculosAparcados.selectionModel.clearSelection()
        tableVehiculosAparcados.items = FXCollections.observableArrayList(viewModel.state.value.listaAparcados)
    }

    private fun initEventos() {
        // Menu
        menuAcercaDe.setOnAction {
            onAcercaDeAction()
        }

        menuSalir.setOnAction {
            onSalirAction()
        }

        menuExportar.setOnAction {
            onExportarAction()
        }

        menuImportar.setOnAction {

        }

        // Botones

        // No necesitan selecionar un vehículo en la tabla
        btnAgregar.setOnAction {
            onAgregarAction()
        }

        btnGestionar.setOnAction {
            onGestionarAction()
        }

        btnAparcar.setOnAction {
            onAparcarAction()
        }

        //Necesita selecionar un vehículo en la tabla
        btnLiberar.setOnAction {
            onLiberarAction()
        }

        // Combo
        comboTipoFiltro.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(it) }
        }

        // Buscador de nombres
        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }

        tableVehiculosAparcados.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTablaSelected(it) }
        }
    }

    private fun onExportarAction() {
        logger.debug { "onExportarAction" }

        FileChooser().run {
            title = "Exportar expedientes"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showSaveDialog(RoutesManager.activeStage)
        }?.let {
            logger.debug { "onSaveAction: $it" }
            RoutesManager.activeStage.scene.cursor = Cursor.WAIT
            viewModel.saveBackupData(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        header = "Datos exportados correctamente a fichero JSON",
                        mensaje = "Se ha exportado tus datos desde el fichero de gestión.\nVehículos y propietarios exportados: ${viewModel.state.value.vehiculos.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = Alert.AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activeStage.scene.cursor = Cursor.DEFAULT
        }
    }

    private fun onTablaSelected(newValue: Aparcamiento) {
        logger.debug { "onTablaSelected: $newValue" }
        viewModel.updateVehiculoAparcadoSeleccionado(newValue)
    }


    private fun showAlertOperacion(
        alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION,
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

    private fun onGestionarAction() {
        logger.debug { "onGestionarAction" }

        RoutesManager.initGestionarStage()
    }

    private fun onLiberarAction() {
        logger.debug { "onLiberarAction" }

        // Comprbar que se ha seleccionado antes
        if (tableVehiculosAparcados.selectionModel.selectedItem == null) {
            return
        }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Vaciar la plaza"
            headerText = "¿Desea liberar la plaza actual?"
            contentText = "Esta acción no se puede deshacer y permitirá a otros ocupar su puesto."
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                viewModel.liberarPlaza().onSuccess {
                    logger.debug { "Plaza liberada correctamente" }
                    showAlertOperation(
                        alerta = Alert.AlertType.INFORMATION,
                        title = "Plaza liberada",
                        header = "La plaza a sido liberada correctamente",
                        mensaje = "Se ha liberado la plaza correctamente del sistema de gestión."
                    )
                }.onFailure {
                    logger.error { "Error al liberar la plaza: ${it.message}" }
                    showAlertOperation(alerta = Alert.AlertType.ERROR, "Error al liberar la plaza", it.message)
                }
            }
        }
    }

    private fun showAlertOperation(
        alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION,
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

    private fun onAparcarAction() {
        logger.debug { "onAparcarAction" }

        RoutesManager.initAparcarStage()
    }

    private fun onAgregarAction() {
        logger.debug { "onAgregarAction" }
        RoutesManager.initAgregarStage()
    }

    private fun onAcercaDeAction() {
        logger.debug { "Pulsado botón 'Acerca de'" }
        RoutesManager.initAboutStage()
    }

    private fun onSalirAction() {
        logger.debug { "onSalirAction" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = "Salir"
            headerText = "Salir del concesionario"
            contentText = "¿Desea salir del concesionario"
        }.showAndWait().ifPresent { buttonType ->
            if (buttonType == ButtonType.OK) {
                Platform.exit()
            }
        }
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected $newValue" }
        filterDataTable()
    }

    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "Filtrando datos de la tabla" }

        tableVehiculosAparcados.items = FXCollections.observableList(
            viewModel.aparcadoFilteredList(comboTipoFiltro.value, textBuscador.text.trim())
        )
    }
}