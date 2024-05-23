package edu.ucne.roomaplicadaii.presentation.tipoTec

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TipoTecViewModel(private val repositoryTipo: TipoTecRepository) : ViewModel() {

    val tiposTec = repositoryTipo.getTipoTec()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun saveTipoTec(tipoTec: TipoTecEntity) {
        viewModelScope.launch {
            repositoryTipo.saveTipoTec(tipoTec)
        }
    }


    companion object {
        fun provideFactory(
            repositoryTipo: TipoTecRepository
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory() {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return TipoTecViewModel(repositoryTipo) as T
                }
            }
    }
}