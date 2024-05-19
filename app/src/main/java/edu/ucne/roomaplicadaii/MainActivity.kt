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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import edu.ucne.roomaplicadaii.data.local.database.TecnicoDb
import edu.ucne.roomaplicadaii.data.local.database.TipoTecDb
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.TecnicoListScreen
import edu.ucne.roomaplicadaii.presentation.TecnicoScreen
import edu.ucne.roomaplicadaii.presentation.TecnicoViewModel
import edu.ucne.roomaplicadaii.presentation.TipoTecScreen
import edu.ucne.roomaplicadaii.presentation.TipoTecViewModel
import edu.ucne.roomaplicadaii.repository.TecnicoRepository
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
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
    private lateinit var tipoTecDb: TipoTecDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        tipoTecDb = Room.databaseBuilder(
            this,
            TipoTecDb::class.java,
            "TipoTec.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val repositoryTipo = TipoTecRepository(tipoTecDb.tipoTecDao())
        enableEdgeToEdge()
        setContent {
            RoomAplicadaIITheme {
                Surface {
                    val viewModel: TecnicoViewModel = viewModel(
                        factory = TecnicoViewModel.provideFactory(repository)
                    )
                    val viewModelTipo: TipoTecViewModel = viewModel(
                        factory = TipoTecViewModel.provideFactory(repositoryTipo)
                    )
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(8.dp)
                        ) {
                            TecnicoScreen(viewModel = viewModel)
                            TipoTecScreen(viewModel = viewModelTipo)
                            TecnicoListScreen(viewModel = viewModel,
                                onDeleteTecnido = {tecnico -> deleteTecnico(tecnico)},
                                onVerTecnico = {}

                            )

                        }

                    }
                }

            }
        }

        fetchTecnicos()
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
