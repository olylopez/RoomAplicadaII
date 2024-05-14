package edu.ucne.roomaplicadaii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import edu.ucne.roomaplicadaii.data.local.database.TecnicoDb
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.TecnicoListScreen
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        enableEdgeToEdge()
        setContent {
            RoomAplicadaIITheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(8.dp)
                    ) {
                        var tecnicoId by remember { mutableStateOf("") }
                        var nombres by remember { mutableStateOf("") }
                        var sueldoHora by remember { mutableStateOf("") }

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                OutlinedTextField(
                                    label = { Text(text = "Nombres") },
                                    value = nombres,
                                    onValueChange = { nombres = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    label = { Text(text = "Sueldo por Hora") },
                                    value = sueldoHora,
                                    onValueChange = { sueldoHora = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            nombres = ""
                                            sueldoHora = ""
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
                                            saveTecnico(
                                                TecnicoEntity(
                                                    tecnicoId = tecnicoId.toIntOrNull(),
                                                    nombres = nombres,
                                                    sueldoHora = sueldoHora.toDoubleOrNull() ?: 0.0
                                                )
                                            )
                                            tecnicoId = ""
                                            nombres = ""
                                            sueldoHora = ""

                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "save button"
                                        )
                                        Text(text = "Guardar")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TecnicoListScreen(tecnicos = tecnicos,
                            onDeleteTecnido = { tecnico -> deleteTecnico(tecnico)},
                            onVerTecnico = {tecnicoSeleccionado ->
                                tecnicoId = tecnicoSeleccionado.tecnicoId.toString()
                                nombres = tecnicoSeleccionado.nombres
                                sueldoHora = tecnicoSeleccionado.sueldoHora.toString()})
                    }
                }
            }
        }

        fetchTecnicos()
    }

    private fun saveTecnico(tecnico: TecnicoEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tecnicoDao().save(tecnico)
            fetchTecnicos()
        }
    }



    private fun fetchTecnicos() {
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tecnicoDao().getAll().collect {
                tecnicos = it
            }
        }
    }

    private var tecnicos by mutableStateOf<List<TecnicoEntity>>(emptyList())

    private fun deleteTecnico(tecnico: TecnicoEntity) {
        val tecnicoId = tecnico.tecnicoId ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tecnicoDao().deleteById(tecnicoId)
        }
    }

}



@Preview(showBackground = true)
@Composable
fun Preview() {
    RoomAplicadaIITheme {

    }
}
