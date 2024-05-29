package edu.ucne.roomaplicadaii.presentation.servicioTec

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity
import edu.ucne.roomaplicadaii.presentation.components.AddButtom
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun ServicioTecListScreen(
    viewModel: ServicioTecViewModel,
    onVerServicioTec: (ServicioTecEntity) -> Unit,
    onAddServicioTec: () -> Unit,
    onDeleteServicioTec: (ServicioTecEntity) -> Unit,
    navController: NavHostController
) {
    val serviciosTec by viewModel.serviciosTec.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Lista - Servicios Técnicos",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = { AddButtom(onAddServicioTec) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(8.dp)
            ) {
                ServicioTecListBody(
                    serviciosTec = serviciosTec,
                    onVerServicioTec = onVerServicioTec,
                    onDeleteServicioTec = onDeleteServicioTec
                )
            }
        }
    }
}

@Composable
fun ServicioTecListBody(
    serviciosTec: List<ServicioTecEntity>,
    onDeleteServicioTec: (ServicioTecEntity) -> Unit,
    onVerServicioTec: (ServicioTecEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        if (serviciosTec.isNotEmpty()) {
            Row {
                Text(
                    text = "Fecha",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(0.200f)
                )

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(0.400f)
                )

                Text(
                    text = "Técnico ID",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(0.200f)
                )

                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(0.200f)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(serviciosTec) { servicioTec ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { onVerServicioTec(servicioTec) }
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(text = servicioTec.fecha.toString(), modifier = Modifier.weight(0.200f))
                        Text(text = servicioTec.descripcion ?: "", modifier = Modifier.weight(0.400f))
                        Text(text = servicioTec.tecnicoId?.toString() ?: "", modifier = Modifier.weight(0.200f))
                        Text(text = servicioTec.total?.toString() ?: "", modifier = Modifier.weight(0.200f))

                        IconButton(
                            onClick = { onDeleteServicioTec(servicioTec) },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete button"
                                )
                            }
                        )

                    }
                }
            }
        }
    }
}
