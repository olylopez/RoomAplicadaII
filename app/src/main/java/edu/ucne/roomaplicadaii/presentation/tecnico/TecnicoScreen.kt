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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme
import kotlinx.coroutines.launch


@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tipos by viewModel.tipos.collectAsStateWithLifecycle(emptyList())
    viewModel.tecnicos.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Registro Técnicos",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )}
        ){
            Column(
                modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)) {
                    TecnicoBory(
                        uiState = uiState,
                        tipos = tipos,
                        onNombresChanged = viewModel::onNombresChanged,
                        onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
                        onTipoTecChanged = viewModel::onTipoTecChanged,
                        onSaveTecnico = {viewModel.saveTecnico()}
                    )

            }

        }
    }
}

@Composable
fun TecnicoBory(
    uiState: TecnicoUIState,
    tipos: List<TipoTecEntity>,
    onSueldoHoraChanged: (String)->Unit,
    onNombresChanged:  (String)->Unit,
    onTipoTecChanged: (String) -> Unit,
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
            tipos = emptyList(),
            onSaveTecnico = {},
            onNombresChanged ={},
            onSueldoHoraChanged = {},
            onTipoTecChanged = {}
        )
    }
}