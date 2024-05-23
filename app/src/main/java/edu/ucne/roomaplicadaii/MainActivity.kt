package edu.ucne.roomaplicadaii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import edu.ucne.roomaplicadaii.data.local.database.TecnicoDb
import edu.ucne.roomaplicadaii.data.local.database.TipoTecDb
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoListScreen
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoScreen
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoViewModel
import edu.ucne.roomaplicadaii.repository.TecnicoRepository
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

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

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList) {

                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository,0) },
                            onDeleteTecnido = { tecnico -> deleteTecnico(tecnico) },
                            onVerTecnico =  {
                            navController.navigate(Screen.Tecnico(it.tecnicoId ?:0 ))
                        })

                    }
                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(viewModel = viewModel { TecnicoViewModel(repository,args.tecnicoId) })
                    }

                }
                /*Surface {
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
                }*/

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
sealed class Screen {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    RoomAplicadaIITheme {

    }
}
