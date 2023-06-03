package dev.sergiosf.proyectoaparcamiento.viewmodels

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiosf.proyectoaparcamiento.errors.AparcadoError
import dev.sergiosf.proyectoaparcamiento.models.Aparcamiento
import dev.sergiosf.proyectoaparcamiento.models.Profesor
import dev.sergiosf.proyectoaparcamiento.models.Vehiculo
import dev.sergiosf.proyectoaparcamiento.repositories.aparcamiento.AparcamientoRepository
import dev.sergiosf.proyectoaparcamiento.repositories.profesor.ProfesorRepository
import dev.sergiosf.proyectoaparcamiento.repositories.vehiculo.VehiculosRepository
import dev.sergiosf.proyectoaparcamiento.service.storage.StorageAparcamiento
import javafx.beans.property.SimpleObjectProperty
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AparcamientoViewModels(
    private val repositoryProfesor: ProfesorRepository,
    private val repositoryVehiculo: VehiculosRepository,
    private val aparcamientoRepository: AparcamientoRepository,
    private val storageAparcamiento: StorageAparcamiento
) {

    val state = SimpleObjectProperty(AparcamientoState())


    init {
        logger.debug { "Inicializando AparcamientoViewModel" }
        loadDataFromRepository()
        loadTypes()
    }

    fun loadDataFromRepository() {
        logger.debug { "Cargando vehículos y propietarios del repositorio" }
        val listaVehiculos = repositoryVehiculo.findAll()
        val listaProfesores = repositoryProfesor.findAll()
        val listaVehiculosAparcados = aparcamientoRepository.findAll()

        val listaCombinada = listaVehiculos.zip(listaProfesores)

        listaCombinada.forEach {
            print(it.first)
            print(it.second)
            println()
        }
        updateState(listaVehiculos, listaProfesores, listaCombinada)
        updateStateAparcado(listaVehiculosAparcados)
    }

    private fun updateState(
        listaVehiculos: List<Vehiculo>,
        listaProfesores: List<Profesor>,
        listaCombinada: List<Pair<Vehiculo, Profesor>>
    ) {
        logger.debug { "Actualizando estado de la Aplicación" }

        state.value = state.value.copy(
            profesor = listaProfesores.sortedBy { it.dni },
            vehiculos = listaVehiculos.sortedBy { it.dniPropietario },
            listaCombinada = listaCombinada
        )
    }

    // Liberar una plaza ocupada en el estado y repositorio
    fun liberarPlaza(): Result<Unit, AparcadoError> {
        logger.debug { "Liberando plaza" }
        // Hay que eliminar su imagen, primero siempre una copia!!!
        val vehiculoAparcado = state.value.vehiculoSeleccionado.copy()
        // Para evitar que cambien en la selección!!!

        // Borramos del repositorio
        aparcamientoRepository.deleteByMatricula(vehiculoAparcado.matricula)
        // Actualizamos la lista
        // Podriamos cargar del repositorio otra vez, si fuera concurente o
        // conectada a un servidor remoto debería hacerlo así
        updateStateAparcado(state.value.listaAparcados.filter { it.matricula != vehiculoAparcado.matricula })
        return Ok(Unit)
    }

    private fun updateStateAparcado(listaVehiculosAparcados: List<Aparcamiento>) {
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
        val vehiculos: List<Vehiculo> = listOf(),
        val profesor: List<Profesor> = listOf(),
        val listaCombinada: List<Pair<Vehiculo, Profesor>> = mutableListOf(),
        val listaAparcados: List<Aparcamiento> = mutableListOf(),

        // Estadísticas
        val numVehiculosElectricos: Int = 0,
        val numCapacidad: Int = 0,

        // siempre que cambia el tipo de operacion, se actualiza el alumno
        val vehiculoSeleccionado: VehiculoYPropietarioFormulario = VehiculoYPropietarioFormulario(), // Vehiculo seleccionado en tabla

        // Creo que esto no lo voy a usar
        //val tipoOperacion: TipoOperacion = TipoOperacion.NUEVO // Tipo de operacion
    )

//    enum class TipoOperacion(val value: String) {
//        NUEVO("Nuevo"), EDITAR("Editar")
//    }

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
                    Vehiculo.TipoVehiculo.NONE.value,
                    Vehiculo.TipoVehiculo.COMBUSTION.value,
                    Vehiculo.TipoVehiculo.HIBRIDO.value,
                    Vehiculo.TipoVehiculo.ELECTRICO.value
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


    enum class TipoFiltro(val value: String) {
        TODOS("Todos/as"), ELECTRICO("Eléctrico/Híbrido"), COMBUSTION("Combustion")
    }
}
