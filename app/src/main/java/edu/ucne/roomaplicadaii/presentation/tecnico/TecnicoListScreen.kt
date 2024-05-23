package edu.ucne.roomaplicadaii.presentation.tecnico

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.components.TopAppBar
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onDeleteTecnido: (TecnicoEntity) -> Unit

) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    TecnicoListBory(
        tecnicos = tecnicos,
        onVerTecnico = onVerTecnico,
        onDeleteTecnido = onDeleteTecnido
    )
}

@Composable
fun TecnicoListBory(
    tecnicos: List<TecnicoEntity>,
    onDeleteTecnido: (TecnicoEntity) -> Unit,
    onVerTecnico: (TecnicoEntity) -> Unit

    ){
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = "Lista De Tecnicos") }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(tecnicos) { tecnico ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                        Text(text = tecnico.nombres, modifier = Modifier.weight(0.400f))
                        Text(
                            text = " -RD " + tecnico.sueldoHora.toString(),
                            modifier = Modifier.weight(0.40f)
                        )
                        IconButton(
                            onClick = { onDeleteTecnido(tecnico) },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete button"
                                )
                            }
                        )
                        IconButton(
                            onClick = { onVerTecnico(tecnico) },
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
@Preview
@Composable
private fun TecnicoListPreview(){
    val tecnicos = listOf(
        TecnicoEntity(
            nombres = "Oly Lopez",
            sueldoHora = 156.6
        )
    )
    RoomAplicadaIITheme {

    }
}





