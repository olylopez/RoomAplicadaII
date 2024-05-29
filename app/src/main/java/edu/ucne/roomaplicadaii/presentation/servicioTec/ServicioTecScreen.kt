package edu.ucne.roomaplicadaii.presentation.servicioTec

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
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.Screen
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.components.DatePicker
import edu.ucne.roomaplicadaii.presentation.components.DropDownInput
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun ServicioTecScreen(
    viewModel: ServicioTecViewModel,
    navController: NavHostController
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tecnicoUIState by viewModel.tecnicos.collectAsStateWithLifecycle()
    viewModel.serviciosTec.collectAsStateWithLifecycle()
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
                    .padding(8.dp)
            ) {
                ServicioTecBory(
                    uiState = uiState,
                    tecnicoUIState = tecnicoUIState,
                    onFechaChanged = viewModel::onFechaChanged,
                    onTecnicoChanged = viewModel::onTecnicoChanged,
                    onClienteChanged = viewModel::onClienteChanged,
                    onDescripcionChanged = viewModel::onDescripcionChanged,
                    onTotalChanged = viewModel::onTotalChanged,
                    onSaveServicioTec = { viewModel.saveServicioTec() },
                    onNewServicioTec = {viewModel.newServicioTec()},
                    onValidation = viewModel::validation,
                    navController = navController
                )
            }
        }

        }
}

@Composable
fun ServicioTecBory(
    uiState: ServicioTecUIState,
    tecnicoUIState: List<TecnicoEntity>,
    onFechaChanged: (Date) -> Unit,
    onTecnicoChanged: (Int) -> Unit,
    onClienteChanged: (String) -> Unit,
    onDescripcionChanged: (String) -> Unit,
    onTotalChanged: (String) -> Unit,
    onSaveServicioTec: () -> Unit,
    onNewServicioTec: () -> Unit,
    onValidation: () -> Boolean,
    navController: NavHostController
){
    var clienteVacio by remember { mutableStateOf(false) }
    var descripcionVacia by remember { mutableStateOf(false) }
    var totalInvalido by remember { mutableStateOf(false) }
    var guardo by remember { mutableStateOf(false) }
    var errorGuardar by remember { mutableStateOf(false) }
    var fecha by remember { mutableStateOf(uiState.fecha) }
    var selectedTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    DatePicker(
                        selectedDate = uiState.fecha,
                        onDateSelected = onFechaChanged,
                        )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = uiState.cliente,
                        onValueChange = {
                            onClienteChanged(it)
                            clienteVacio = it.isEmpty()
                        },
                        isError = clienteVacio,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (clienteVacio) {
                        Text(
                            text = "Campo Obligatorio",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = {
                            onDescripcionChanged(it)
                            descripcionVacia = it.isEmpty()
                        },
                        isError = descripcionVacia,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (descripcionVacia) {
                        Text(
                            text = "Campo Obligatorio",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        label = { Text(text = "Total") },
                        value = uiState.total?.toString() ?: "",
                        onValueChange = {
                            onTotalChanged(it)
                            totalInvalido = it.isEmpty() || it.toDoubleOrNull() == null || it.toDouble() <= 0
                        },
                        isError = totalInvalido,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (totalInvalido) {
                        Text(
                            text = "Debe ser un número mayor que 0",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    DropDownInput(
                        items = tecnicoUIState,
                        label = "Técnico",
                        itemToString = { "${it.tecnicoId} - ${it.nombres} - ${it.tipo}" },
                        onItemSelected = { tecnico ->
                            selectedTecnico = tecnico
                            tecnico.tecnicoId?.let { onTecnicoChanged(it) }
                        },
                        selectedItem = selectedTecnico?.let { "${it.tecnicoId} - ${it.nombres} - ${it.tipo}" } ?: "",
                        isError = selectedTecnico == null
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp), // Añade padding horizontal
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                onNewServicioTec()
                                clienteVacio = false
                                descripcionVacia = false
                                totalInvalido = false
                                uiState.cliente = ""
                                uiState.total = null
                                uiState.descripcion = ""
                                uiState.tecnicoId = null
                            },
                            modifier = Modifier.weight(1f) // Usa weight para que ambos botones compartan el espacio disponible
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "new button"
                            )
                            Text("Nuevo")
                        }
                        Button(
                            onClick = {
                                if (onValidation()) {
                                    onSaveServicioTec()
                                    guardo = true
                                    clienteVacio = false
                                    descripcionVacia = false
                                    totalInvalido = false
                                    navController.navigate(Screen.ServicioTecList)
                                } else {
                                    errorGuardar = true
                                    clienteVacio = uiState.clienteError
                                    descripcionVacia = uiState.descripcionError
                                    totalInvalido = uiState.totalError
                                }
                            },
                            modifier = Modifier.weight(1f) // Usa weight para que ambos botones compartan el espacio disponible
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "save button"
                            )
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}