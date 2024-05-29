package edu.ucne.roomaplicadaii.presentation.servicioTec

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.repository.ServicioTecRepository
import edu.ucne.roomaplicadaii.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ServicioTecViewModel(private val repository: ServicioTecRepository,
                           private val tecnicoRepository: TecnicoRepository,
                           private val servicioId: Int) : ViewModel()
{
    var uiState = MutableStateFlow(ServicioTecUIState())
        private set

    private val _tecnicos = MutableStateFlow<List<TecnicoEntity>>(emptyList())
    val tecnicos: StateFlow<List<TecnicoEntity>> = _tecnicos

    val serviciosTec = repository.getServicioTec()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onFechaChanged(fecha: Date) {
        uiState.update {
            it.copy(fecha = fecha)
        }
    }

    fun onTecnicoChanged(tecnicoId: Int) {
        uiState.update { it.copy(tecnicoId = tecnicoId) }
    }

    fun onClienteChanged(cliente: String) {
        uiState.update {
            it.copy(cliente = cliente)
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun onTotalChanged(totalStr: String) {
        val regex = Regex("[0-9]{0,7}\\.?[0-9]{0,2}")
        if (totalStr.matches(regex)) {
            val total = totalStr.toDoubleOrNull() ?: 0.0
            uiState.update {
                it.copy(total = total)
            }
        }
    }

    init {
        fetchTecnicos()
        viewModelScope.launch {
            val servicioTec = repository.getServicioTec(servicioId)
            servicioTec?.let {
                uiState.update {
                    it.copy(
                        servicioId = servicioTec.servicioId,
                        fecha = servicioTec.fecha,
                        tecnicoId = servicioTec.tecnicoId,
                        cliente = servicioTec.cliente ?: "",
                        descripcion = servicioTec.descripcion ?: "",
                        total = servicioTec.total
                    )
                }
            }
        }
    }

    private fun fetchTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAllTecnicos().collect { tecnicosList ->
                _tecnicos.value = tecnicosList
            }
        }
    }

    fun saveServicioTec() {
        viewModelScope.launch {
            repository.saveServicioTec(uiState.value.toEntity())
        }
    }

    fun validation(): Boolean {
        uiState.value.clienteError = uiState.value.cliente.isEmpty()
        uiState.value.descripcionError = uiState.value.descripcion.isEmpty()
        uiState.value.totalError = (uiState.value.total ?: 0.0) <= 0.0
        uiState.update {
            it.copy(
                saveSuccess = !uiState.value.clienteError &&
                        !uiState.value.descripcionError &&
                        !uiState.value.totalError
            )
        }
        return uiState.value.saveSuccess
    }

    fun newServicioTec() {
        viewModelScope.launch {
            uiState.value = ServicioTecUIState()
        }
    }
}

data class ServicioTecUIState(
    val servicioId: Int? = null,
    var fecha: Date = Date(),
    var tecnicoId: Int? = null,
    var cliente: String = "",
    var clienteError: Boolean = false,
    var descripcion: String = "",
    var descripcionError: Boolean = false,
    var total: Double? = null,
    var totalError: Boolean = false,
    var saveSuccess: Boolean = false

)

fun ServicioTecUIState.toEntity(): ServicioTecEntity {
    return ServicioTecEntity(
        servicioId = servicioId,
        fecha = fecha,
        tecnicoId = tecnicoId,
        cliente = cliente,
        descripcion = descripcion,
        total = total
    )
}
