package edu.ucne.roomaplicadaii.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import org.w3c.dom.Text

@Composable
fun TecnicoListScreen(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onDeleteTecnido: (TecnicoEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(tecnicos) { tecnico ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = tecnico.nombres, modifier = Modifier.weight(0.400f))
                    Text(text = " -RD " + tecnico.sueldoHora.toString(), modifier = Modifier.weight(0.40f))
                    IconButton(
                        onClick = { onDeleteTecnido(tecnico) },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete button"
                            )
                        }
                    )
                    IconButton(
                        onClick = { onVerTecnico(tecnico) },
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




