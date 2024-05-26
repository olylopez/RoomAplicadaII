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


    init {
        viewModelScope.launch {
            val tipoTec = repositoryTipo.getTipoTec(tipoId)

            tipoTec?.let {
                uiState.update {
                    it.copy(
                        tipoId = tipoTec.tipoId,
                        descripcion = tipoTec.descripcion,
                    )
                }
            }
        }
    }

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


    fun saveTipoTec() {
        viewModelScope.launch {
            repositoryTipo.saveTipoTec(uiState.value.toEntity())
        }
    }
    fun newTipoTecnico(){
        viewModelScope.launch {
            uiState.value = TipoTecUIState()
        }
    }
    private fun descripcionExists(descripcion: String, id: Int?): Boolean {
        return tiposTec.value.any { it.descripcion.replace(" ", "").uppercase() == descripcion.replace(" ", "").uppercase() && it.tipoId != id }
    }
    fun validation(): Boolean {
        uiState.value.descripcionError = (uiState.value.descripcion.isEmpty())
        uiState.value.descripcionRepetida = descripcionExists(uiState.value.descripcion, uiState.value.tipoId)
        uiState.update {
            it.copy( saveSuccess =  !uiState.value.descripcionError && !uiState.value.descripcionRepetida)
        }
        return uiState.value.saveSuccess
    }



}
data class TipoTecUIState(
    val tipoId: Int? = null,
    var descripcion: String = "",
    var descripcionError: Boolean = false,
    var descripcionRepetida: Boolean = false,
    var saveSuccess: Boolean = false
)
fun TipoTecUIState.toEntity(): TipoTecEntity {
    return TipoTecEntity(
        tipoId = tipoId,
        descripcion = descripcion
    )
}