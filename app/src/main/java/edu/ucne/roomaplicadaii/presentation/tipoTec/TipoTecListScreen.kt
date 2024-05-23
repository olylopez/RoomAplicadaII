package edu.ucne.roomaplicadaii.presentation.tipoTec

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity

@Composable
fun TipoTecListScreen(
    viewModel: TipoTecViewModel,
    onVerTipoTec: (TipoTecEntity) -> Unit,
    onDeleteTipoTec: (TipoTecEntity) -> Unit

) {
    val tiposTec by viewModel.tiposTec.collectAsStateWithLifecycle()
    TipoTecListBory(
        tiposTec = tiposTec,
        onVerTipoTec = onVerTipoTec,
        onDeleteTipoTec = onDeleteTipoTec
    )
}

@Composable
fun TipoTecListBory(
    tiposTec: List<TipoTecEntity>,
    onDeleteTipoTec: (TipoTecEntity) -> Unit,
    onVerTipoTec: (TipoTecEntity) -> Unit

){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(tiposTec) { tipo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(text = tipo.tipoId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = tipo.descripcion, modifier = Modifier.weight(0.400f))

                    IconButton(
                        onClick = { onDeleteTipoTec(tipo) },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete button"
                            )
                        }
                    )
                    IconButton(
                        onClick = { onVerTipoTec(tipo) },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit button"
                            )
                        }
                    )
                }
            }
        }
    }
}