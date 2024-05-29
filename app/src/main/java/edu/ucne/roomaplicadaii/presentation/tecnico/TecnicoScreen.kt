package edu.ucne.roomaplicadaii.presentation.tecnico

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.Screen
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.presentation.components.DropDownInput
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
                        onSaveTecnico = {viewModel.saveTecnico()},
                        onNewTecnico = {viewModel.newTecnico()},
                        onValidation = viewModel::validation,
                        navController = navController
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
    onNewTecnico: () -> Unit,
    onValidation: () -> Boolean,
    navController: NavHostController

    ) {
    var nombreVacio by remember { mutableStateOf(false) }
    var sueldoHoraNoValido by remember {mutableStateOf(false)}
    var sinTipo by remember {mutableStateOf(false)}
    var guardo by remember { mutableStateOf(false) }
    var errorGuardar by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf<TipoTecEntity?>(null) }



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
////NOMRBE
                    OutlinedTextField(
                        label = { Text(text = "Nombre") },
                        value = uiState.nombres,
                        onValueChange =  onNombresChanged,
                        isError = nombreVacio,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if(nombreVacio){
                        Text(
                            text = "Campo Obligatorio.",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
///SUELDO HORA
                    OutlinedTextField(
                        label = { Text(text = "Sueldo por Hora") },
                        value = uiState.sueldoHora.toString().replace("null", ""),
                        onValueChange = onSueldoHoraChanged,
                        isError = sueldoHoraNoValido,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if(sueldoHoraNoValido){
                        Text(
                            text = "Sueldo por Hora debe ser > 0.0",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    DropDownInput(
                        items = tipos,
                        label = "Tipos Técnico",
                        itemToString = { it.descripcion},
                        onItemSelected = {
                            selectedTipo = it
                            onTipoTecChanged(it.descripcion )
                        },
                        selectedItem = uiState.tipo,
                        isError = sinTipo
                    )
                    if(sinTipo){
                        Text(
                            text = "Debe Seleccionar un Tipo para el Tecnico.",
                            color = Color.Red,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                onNewTecnico()
                                nombreVacio = false
                                sueldoHoraNoValido = false
                                sinTipo = false
                                uiState.nombres = " "
                                uiState.sueldoHora = null

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
                                if (onValidation()) {
                                    onSaveTecnico()
                                    guardo = true
                                    nombreVacio = false
                                    sueldoHoraNoValido = false
                                    sinTipo = false
                                    navController.navigate(Screen.TecnicoList)
                                }
                                else{
                                    errorGuardar = true
                                    nombreVacio = uiState.nombresError
                                    sueldoHoraNoValido = uiState.sueldoHoraError
                                    sinTipo = uiState.tipoError
                                }
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
            onTipoTecChanged = {},
            onNewTecnico = {},
            onValidation = {false},
            navController = NavHostController(LocalContext.current),
        )
    }
}