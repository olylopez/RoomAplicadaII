package edu.ucne.roomaplicadaii.presentation.tipoTec

import androidx.compose.foundation.background
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
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.presentation.components.AddButtom
import edu.ucne.roomaplicadaii.presentation.components.NavigationDrawer
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun TipoTecListScreen(
    viewModel: TipoTecViewModel,
    onVerTipoTec: (TipoTecEntity) -> Unit,
    onAddTipoTec: () -> Unit,
    onDeleteTipoTec: (TipoTecEntity) -> Unit,
    navController: NavHostController

) {
    val tiposTec by viewModel.tiposTec.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title="Lista - Tipos de Técnicos",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = { AddButtom(onAddTipoTec) }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(8.dp)
            ){
                TipoTecListBory(
                    tiposTec = tiposTec,
                    onVerTipoTec = onVerTipoTec,
                    onDeleteTipoTec = onDeleteTipoTec
                )
            }
        }
    }
}

@Composable
fun TipoTecListBory(
    tiposTec: List<TipoTecEntity>,
    onDeleteTipoTec: (TipoTecEntity) -> Unit,
    onVerTipoTec: (TipoTecEntity) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        if (tiposTec.isNotEmpty()) {
            Row {
                Text(
                    text = "Id",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(0.100f)
                )

                Text(
                    text = "Descripcion",
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
                items(tiposTec) {tipoTec ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(text = tipoTec.tipoId.toString(), modifier = Modifier.weight(0.100f))
                        Text(text = tipoTec.descripcion, modifier = Modifier.weight(0.200f))

                        IconButton(
                            onClick = { onDeleteTipoTec(tipoTec) },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete button"
                                )
                            }
                        )
                        IconButton(
                            onClick = { onVerTipoTec(tipoTec) },
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


}