package edu.ucne.roomaplicadaii.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme

@Composable
fun TipoTecScreen
            (viewModel: TipoTecViewModel
){
    val tiposTec by viewModel.tiposTec.collectAsStateWithLifecycle()
    TipoTecBory(
        onSavedTipoTec ={tipoTec ->
            viewModel.saveTipoTec(tipoTec)
        }
    )
}

@Composable
fun TipoTecBory(onSavedTipoTec: (TipoTecEntity) -> Unit){
    var tipoId by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Tipo Tecnicos") },
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        descripcion = ""

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "new button"
                    )
                    Text(text = "Nuevo")
                }

                OutlinedButton(
                    onClick = {
                        onSavedTipoTec(
                            TipoTecEntity(
                                tipoId = tipoId.toIntOrNull(),
                                descripcion = descripcion,

                            )
                        )
                        tipoId = ""
                        descripcion = ""

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }
            }
        }
    }
}

@Preview
@Composable
private fun TipoTecPreview() {
    RoomAplicadaIITheme {
        TipoTecBory() {
        }
    }
}