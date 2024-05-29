package edu.ucne.roomaplicadaii.presentation.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.repository.TecnicoRepository
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TecnicoViewModel(
    private val repository: TecnicoRepository,
    tipoRepository: TipoTecRepository,
    private val tecnicoId: Int) :
    ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set


    val tecnicos = repository.getTenicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    fun onSueldoHoraChanged(sueldoHoraStr: String) {
        val regex = Regex("[0-9]{0,7}\\.?[0-9]{0,2}")
        if (sueldoHoraStr.matches(regex)) {
            val sueldoHora = sueldoHoraStr.toDoubleOrNull()
            uiState.update {
                it.copy(
                    sueldoHora = sueldoHora,
                )
            }
        }
    }
    fun onNombresChanged(nombres: String){
        uiState.update {
            it.copy(nombres = nombres)
        }
    }
    fun onTipoTecChanged(tipo: String) {
        uiState.update {
            it.copy(tipo = tipo)
        }
    }
    fun newTecnico() {
        viewModelScope.launch {
            uiState.value = TecnicoUIState()
        }
    }
    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId,
                        nombres = tecnico.nombres?: "",
                        sueldoHora = tecnico.sueldoHora,
                        tipo = tecnico.tipo?: ""


                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            repository.saveTecnico(uiState.value.toEntity())
        }
    }

    val tipos = tipoRepository.getTipoTec()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    fun validation(): Boolean {
        uiState.value.nombresError = (uiState.value.nombres.isEmpty())
        uiState.value.sueldoHoraError = ((uiState.value.sueldoHora ?: 0.0) <= 0.0)
        uiState.value.tipoError = (uiState.value.tipo.isEmpty())
        uiState.update {
            it.copy( saveSuccess =  !uiState.value.nombresError &&
                    !uiState.value.sueldoHoraError &&
                    !uiState.value.tipoError

            )
        }
        return uiState.value.saveSuccess
    }
}
data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombres: String = "",
    var nombresError: Boolean = false,
    var sueldoHora: Double? = null,
    var sueldoHoraError: Boolean = false,
    var tipo: String = "",
    var tipoError: Boolean = false,
    var saveSuccess: Boolean = false
)
fun TecnicoUIState.toEntity(): TecnicoEntity {
    return TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldoHora = sueldoHora,
        tipo = tipo
    )
}