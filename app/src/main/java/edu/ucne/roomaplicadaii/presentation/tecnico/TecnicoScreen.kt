package edu.ucne.roomaplicadaii.presentation.tecnico

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme


@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoBory(
        uiState = uiState,
        onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
        onNombresChanged= viewModel::onNombresChanged,
        onSaveTecnico = {
            viewModel.saveTecnico()
        }
    )
}

@Composable
fun TecnicoBory(
    uiState: TecnicoUIState,
    onSueldoHoraChanged: (String)->Unit,
    onNombresChanged:  (String)->Unit,
    onSaveTecnico: () -> Unit,

    ) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Nombres") },
                        value = uiState.nombres,
                        onValueChange = onNombresChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        label = { Text(text = "Sueldo por Hora") },
                        value = uiState.sueldoHora.toString().replace("null", ""),
                        onValueChange = onSueldoHoraChanged,
                        modifier = Modifier.fillMaxWidth()
                    )



                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {

                                uiState.nombres = " "
                                uiState.sueldoHora = 0.0

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
                                onSaveTecnico()
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
}

@Preview
@Composable
private fun TecnicoPreview() {
    RoomAplicadaIITheme {
        TecnicoBory(
            uiState = TecnicoUIState(),
            onSaveTecnico = {},
            onNombresChanged ={},
            onSueldoHoraChanged = {}
        )
    }
}