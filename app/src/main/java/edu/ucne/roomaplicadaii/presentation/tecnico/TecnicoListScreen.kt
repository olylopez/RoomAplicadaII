package edu.ucne.roomaplicadaii.presentation.tecnico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.components.AddButtom
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onDeleteTecnido: (TecnicoEntity) -> Unit,
    onAddTecnico: () -> Unit,
    navController: NavHostController

) {

    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopAppBar(
                title = "Técnicos",
                onMenuClick = { scope.launch { drawerState.open() } }
            )},
            floatingActionButton = { AddButtom(onAddTecnico) }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .padding(it)
            ) {
                TecnicoListBory(
                    tecnicos = tecnicos,
                    onDeleteTecnido = onDeleteTecnido,
                    onVerTecnico = onVerTecnico

                )
            }
        }
    }
}

@Composable
fun TecnicoListBory(
    tecnicos: List<TecnicoEntity>,
    onDeleteTecnido: (TecnicoEntity) -> Unit,
    onVerTecnico: (TecnicoEntity) -> Unit

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {

            if (tecnicos.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Id",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Nombres",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(3f)
                    )
                    Text(
                        text = "Sueldo por Hora",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = "Tipo",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tecnicos) { tecnico ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(1f))
                            tecnico.nombres?.let { Text(text = it, modifier = Modifier.weight(3f)) }
                            Text(text = "$${tecnico.sueldoHora}", modifier = Modifier.weight(2f))
                            tecnico.tipo?.let { Text(text = it, modifier = Modifier.weight(1f)) }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { onDeleteTecnido(tecnico) },
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete button"
                                )
                            }
                            IconButton(
                                onClick = { onVerTecnico(tecnico) },
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "edit button"
                                )
                            }
                        }
                    }
                }
            }
        }
    }







