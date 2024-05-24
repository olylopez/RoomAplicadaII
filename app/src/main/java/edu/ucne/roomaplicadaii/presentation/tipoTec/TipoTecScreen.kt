package edu.ucne.roomaplicadaii.presentation.tipoTec

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TipoTecScreen(
    viewModel: TipoTecViewModel
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TipoTecBory(
        uiState = uiState,
        onDescripcionChanged = viewModel::onDescriptionChanged,
        onSaveTipoTec = {viewModel.saveTipoTec()}
    )
}

@Composable
fun TipoTecBory(
    uiState: TipoTecUIState,
    onDescripcionChanged: (String)->Unit,
    onSaveTipoTec: () -> Unit,

    )
    {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(modifier = Modifier) {
                Column(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                }
                OutlinedTextField(
                    label = { Text(text = "Tipo Tecnicos") },
                    value = uiState.descripcion,
                    onValueChange = onDescripcionChanged,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            uiState.descripcion = ""

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
                            onSaveTipoTec()
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
}


