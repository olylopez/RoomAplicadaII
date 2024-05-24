package edu.ucne.roomaplicadaii.presentation.tipoTec

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TipoTecViewModel(private val repositoryTipo: TipoTecRepository,
    private val tipoId: Int) : ViewModel() {
    var uiState = MutableStateFlow(TipoTecUIState())
        private set



    val tiposTec = repositoryTipo.getTipoTec()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )



    fun onDescriptionChanged(descripcion: String){
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    init {
        viewModelScope.launch {
            val tipoTec = repositoryTipo.getTipoTec(tipoId)

            tipoTec?.let {
                uiState.update {
                    it.copy(
                        tipoId = tipoTec.tipoId?: 0,
                        descripcion = tipoTec.descripcion?: "",
                    )
                }
            }
        }
    }

    fun saveTipoTec() {
        viewModelScope.launch {
            repositoryTipo.saveTipoTec(uiState.value.toEntity())
        }
    }



}
data class TipoTecUIState(
    val tipoId: Int = 0,
    var descripcion: String = "",
    var descripcionError: String? = null
)
fun TipoTecUIState.toEntity(): TipoTecEntity {
    return TipoTecEntity(
        tipoId = tipoId,
        descripcion = descripcion
    )
}