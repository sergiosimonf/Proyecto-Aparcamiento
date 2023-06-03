package dev.sergiosf.proyectoaparcamiento.viewmodels

import com.github.michaelbull.result.*
import dev.sergiosf.proyectoaparcamiento.errors.AparcadoError
import dev.sergiosf.proyectoaparcamiento.errors.PersonaError
import dev.sergiosf.proyectoaparcamiento.errors.VehiculoError
import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento.AparcamientoRepository
import dev.sergiosf.proyectoaparcamiento.repositories.profesor.ProfesorRepository
import dev.sergiosf.proyectoaparcamiento.repositories.vehiculo.VehiculosRepository
import dev.sergiosf.proyectoaparcamiento.service.storage.StorageAparcamiento
import dev.sergiosf.proyectoaparcamiento.validators.validar
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Alert
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AparcamientoViewModels(
    private val repositoryProfesor: ProfesorRepository,
    private val repositoryVehiculo: VehiculosRepository,
    private val repositoryAparcamiento: AparcamientoRepository,
    private val storageAparcamiento: StorageAparcamiento
) {

    val state = SimpleObjectProperty(AparcamientoState())


    init {
        logger.debug { "Inicializando AparcamientoViewModel" }
        loadDataFromRepository()
        loadTypes()
        loadTiposMotor()
    }

    fun loadDataFromRepository() {
        logger.debug { "Cargando vehículos y propietarios del repositorio" }
        val listaVehiculos = repositoryVehiculo.findAll()
        val listaProfesores = repositoryProfesor.findAll()
        val listaVehiculosAparcados = repositoryAparcamiento.findAll()

        updateStateVehiculos(listaVehiculos)
        updateStateProfesores(listaProfesores)
        updateStateAparcado(listaVehiculosAparcados)
    }

    private fun updateStateVehiculos(
        listaVehiculos: List<Vehiculo>
    ) {
        logger.debug { "Actualizando estado de la Aplicación" }

        state.value = state.value.copy(
            vehiculos = listaVehiculos.sortedBy { it.dniPropietario },
        )
    }

    fun deleteVehiculoPropietario(matricula: String) {
        val vehiculo = state.value.vehiculos.filter { it.matricula == matricula }.first()
        val usuario = state.value.profesor.filter { it.dni == vehiculo.dniPropietario }.first()

        repositoryVehiculo.deleteByMatricula(vehiculo.matricula)
        repositoryProfesor.deleteByDni(usuario.dni)
    }

    private fun updateStateProfesores(
        listaProfesores: List<Profesor>,
    ) {
        logger.debug { "Actualizando estado de la Aplicación" }

        state.value = state.value.copy(
            profesor = listaProfesores.sortedBy { it.dni },
        )
    }

    // Liberar una plaza ocupada en el estado y repositorio
    fun liberarPlaza(): Result<Unit, AparcadoError> {
        logger.debug { "Liberando plaza" }

        val vehiculoAparcado = state.value.vehiculoSeleccionado.copy()
        // Para evitar que cambien en la selección!!!

        // Borramos del repositorio
        repositoryAparcamiento.deleteByMatricula(vehiculoAparcado.matricula)
        // Actualizamos la lista
        updateStateAparcado(state.value.listaAparcados.filter { it.matricula != vehiculoAparcado.matricula })
        return Ok(Unit)
    }

    fun ocuparPlaza(aparcamiento: Aparcamiento): Result<Aparcamiento, AparcadoError> {
        logger.debug { "Ocupando una plaza" }

        return aparcamiento.validar().onSuccess {
            val new = repositoryAparcamiento.save(aparcamiento)
            updateStateAparcado(state.value.listaAparcados + new)
            Ok(new)
        }.onFailure { e ->
            Alert(Alert.AlertType.ERROR).apply {
                title = "Datos no validos"
                headerText = "Error al guardar al aparcar el vehiculo"
                contentText = "Hubo un problema al aparcar el vehiculo los datos son invalidos: ${e.message}"
            }.showAndWait()
        }
    }

    fun guardar(formulario: VehiculoYPropietarioFormulario) {
        val newVehiculo = formulario.toVehiculo()
        val newProfesor = formulario.toProfesor()

        guardarVehiculo(newVehiculo)
        guardarProfesor(newProfesor)
    }

    private fun guardarVehiculo(newVehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        return newVehiculo.validar()
            .andThen {
                val new = repositoryVehiculo.save(newVehiculo)
                updateStateVehiculos(state.value.vehiculos + new)
                Ok(new)
            }
    }

    private fun guardarProfesor(newProfesor: Profesor): Result<Profesor, PersonaError> {
        return newProfesor.validar()
            .andThen {
                val new = repositoryProfesor.save(newProfesor)
                updateStateProfesores(state.value.profesor + new)
                Ok(new)
            }
    }

    fun VehiculoYPropietarioFormulario.toProfesor(): Profesor {
        return Profesor(
            dni = this.dni,
            nombre = this.nombre,
            apellido = this.apellido,
        )
    }

    fun VehiculoYPropietarioFormulario.toVehiculo(): Vehiculo {
        return Vehiculo(
            matricula = this.matricula,
            dniPropietario = this.dni,
            marca = this.marca,
            modelo = this.modelo,
            tipoVehiculo = this.tipoVehiculo
        )
    }

    fun isAparcadoByMatricula(matricula: String): Boolean {
        return repositoryAparcamiento.findByMatricula(matricula) == null
    }

    private fun updateStateAparcado(listaVehiculosAparcados: List<Aparcamiento>) {
        logger.debug { "Actualizando estado de los vehículos aparcados" }
        val numElectricos = listaVehiculosAparcados.count { it.isElectrico() }
        val capacidad = listaVehiculosAparcados.count()

        state.value = state.value.copy(
            numVehiculosElectricos = numElectricos,
            numCapacidad = capacidad,
            listaAparcados = listaVehiculosAparcados
        )
    }

    data class AparcamientoState(
        // Los contenedores de colecciones deben ser ObservableList
        val typeVehiculo: List<String> = listOf(),
        val typeMotor: List<String> = listOf(),
        val vehiculos: List<Vehiculo> = listOf(),
        val profesor: List<Profesor> = listOf(),
        val listaAparcados: List<Aparcamiento> = mutableListOf(),

        // Estadísticas
        val numVehiculosElectricos: Int = 0,
        val numCapacidad: Int = 0,

        // siempre que cambia el tipo de operacion, se actualiza el alumno
        val vehiculoSeleccionado: VehiculoYPropietarioFormulario = VehiculoYPropietarioFormulario(), // Vehículo seleccionado en tabla
    )

    data class VehiculoYPropietarioFormulario(
        val dni: String = "",
        val nombre: String = "",
        val apellido: String = "",
        val matricula: String = "",
        val marca: String = "",
        val modelo: String = "",
        val tipoVehiculo: Vehiculo.TipoVehiculo = Vehiculo.TipoVehiculo.NONE
    )

    private fun loadTypes() {
        logger.debug { "Cargando tipos de vehículos" }
        state.value = state.value.copy(
            typeVehiculo = listOf(
                TipoFiltro.TODOS.value,
                TipoFiltro.ELECTRICO.value,
                TipoFiltro.COMBUSTION.value
            )
        )
    }

    private fun loadTiposMotor() {
        logger.debug { "Cargando tipos de motor" }
        state.value = state.value.copy(
            typeMotor = listOf(
                Vehiculo.TipoVehiculo.NONE.value,
                Vehiculo.TipoVehiculo.Combustion.value,
                Vehiculo.TipoVehiculo.Híbrido.value,
                Vehiculo.TipoVehiculo.Eléctrico.value,
            )
        )
    }

    fun aparcadoFilteredList(tipo: String, nombreCompleto: String): List<Aparcamiento> {
        logger.debug { "Filtrando lista de Profesores por nombre" }

        return state.value.listaAparcados
            .filter { aparcado ->
                when(tipo) {
                    TipoFiltro.ELECTRICO.value -> aparcado.isElectrico()
                    TipoFiltro.COMBUSTION.value -> !aparcado.isElectrico()
                    else -> true
                }
            }.filter { aparcado ->
                aparcado.propietario.contains(nombreCompleto, true)
            }
    }

    fun updateVehiculoAparcadoSeleccionado(aparcado: Aparcamiento) {
        logger.debug { "Actualizando estado de Vehiculo aparcado: $aparcado" }

        val vehiculo = state.value.vehiculos.filter { it.matricula == aparcado.matricula }.first()
        val propietario = state.value.profesor.filter { it.dni == vehiculo.dniPropietario }.first()

        val vehiculoYPropietarioFormulario = VehiculoYPropietarioFormulario(
            dni = propietario.dni,
            nombre = propietario.nombre,
            apellido = propietario.apellido,
            matricula = vehiculo.matricula,
            marca = vehiculo.marca,
            modelo = vehiculo.modelo,
            tipoVehiculo= vehiculo.tipoVehiculo
        )

        state.value = state.value.copy(vehiculoSeleccionado = vehiculoYPropietarioFormulario)
    }

    fun editarVehiculoPropietario(formulario: VehiculoYPropietarioFormulario) {
        val newVehiculo = formulario.toVehiculo()
        val newProfesor = formulario.toProfesor()

        editarVehiculo(newVehiculo)
        editarProfesor(newProfesor)
    }

    private fun editarVehiculo(newVehiculo: Vehiculo): Result<Vehiculo, VehiculoError> {
        return newVehiculo.validar()
            .andThen {
                val new = repositoryVehiculo.updateByMatricula(it)
                updateStateVehiculos(state.value.vehiculos + new)
                Ok(new)
            }
    }

    private fun editarProfesor(newProfesor: Profesor): Result<Profesor, PersonaError> {
        return newProfesor.validar()
            .andThen {
                val new = repositoryProfesor.updateByDni(newProfesor)
                updateStateProfesores(state.value.profesor + new)
                Ok(new)
            }
    }


    enum class TipoFiltro(val value: String) {
        TODOS("Todos/as"), ELECTRICO("Eléctrico/Híbrido"), COMBUSTION("Combustion")
    }
}