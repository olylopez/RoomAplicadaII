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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.Screen
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun TipoTecScreen(
    viewModel: TipoTecViewModel,
    navController: NavHostController
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.tiposTec.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Registro - Tipos de Técnicos",
                    onMenuClick = { scope.launch { drawerState.open() } }
                ) }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(8.dp)) {

                    TipoTecBory(
                        uiState = uiState,
                        onDescripcionChanged = viewModel::onDescriptionChanged,
                        onSaveTipoTec = {viewModel.saveTipoTec()},
                        onNewTipoTec = {viewModel.newTipoTecnico()},
                        onValidation = viewModel::validation,
                        navController = navController
                    )
            }
        }
    }

}

@Composable
fun TipoTecBory(
    uiState: TipoTecUIState,
    onDescripcionChanged: (String)->Unit,
    onSaveTipoTec: () -> Unit,
    onNewTipoTec: () -> Unit,
    onValidation: () -> Boolean,
    navController: NavHostController

    ) {

    var descripcionVacio by remember { mutableStateOf(false) }
    var descripcionRepetida by remember { mutableStateOf(false) }
    var guardo by remember { mutableStateOf(false) }
    var errorGuardar by remember { mutableStateOf(false) }

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
                    isError = descripcionVacio || descripcionRepetida,
                    modifier = Modifier.fillMaxWidth()
                )
                if(descripcionRepetida){
                    Text(
                        text = "Tipo Técnico ya existe.",
                        color = Color.Red,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp
                    )
                }
                if(descripcionVacio){
                    Text(
                        text = "Campo Obligatorio.",
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
                            onNewTipoTec()
                            descripcionVacio = false
                            descripcionRepetida= false

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
                                onSaveTipoTec()
                                guardo = true
                                descripcionVacio = false
                                descripcionRepetida = false
                                navController.navigate(Screen.TipoTecList)
                            }
                            else{
                                errorGuardar = true
                                descripcionVacio = uiState.descripcionError
                                descripcionRepetida = uiState.descripcionRepetida
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


