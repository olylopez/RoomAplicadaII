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
            val sueldoHora = sueldoHoraStr.toDoubleOrNull() ?: 0.0
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
    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId?: 0,
                        nombres = tecnico.nombres?: "",
                        sueldoHora = tecnico.sueldoHora?: 0.0,
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
    /*companion object {
        fun provideFactory(
            repository: TecnicoRepository
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return TecnicoViewModel(repository, 0) as T
                }
            }
    }*/

}
data class TecnicoUIState(
    val tecnicoId: Int = 0,
    var nombres: String = "",
    var nombresError: String? = null,
    var sueldoHora: Double = 0.0,
    var sueldoHoraError: Double? = 0.0,
    var tipo: String = "",
    var tipoError: String? = ""
)
fun TecnicoUIState.toEntity(): TecnicoEntity {
    return TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldoHora = sueldoHora,
        tipo = tipo
    )
}