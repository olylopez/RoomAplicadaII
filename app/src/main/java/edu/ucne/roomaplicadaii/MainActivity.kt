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
import edu.ucne.roomaplicadaii.data.local.entities.ServicioTecEntity
import edu.ucne.roomaplicadaii.data.local.entities.TecnicoEntity
import edu.ucne.roomaplicadaii.data.local.entities.TipoTecEntity
import edu.ucne.roomaplicadaii.presentation.servicioTec.ServicioTecListScreen
import edu.ucne.roomaplicadaii.presentation.servicioTec.ServicioTecScreen
import edu.ucne.roomaplicadaii.presentation.servicioTec.ServicioTecViewModel
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoListScreen
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoScreen
import edu.ucne.roomaplicadaii.presentation.tecnico.TecnicoViewModel
import edu.ucne.roomaplicadaii.presentation.tipoTec.TipoTecListScreen
import edu.ucne.roomaplicadaii.presentation.tipoTec.TipoTecScreen
import edu.ucne.roomaplicadaii.presentation.tipoTec.TipoTecViewModel
import edu.ucne.roomaplicadaii.repository.ServicioTecRepository
import edu.ucne.roomaplicadaii.repository.TecnicoRepository
import edu.ucne.roomaplicadaii.repository.TipoTecRepository
import edu.ucne.roomaplicadaii.ui.theme.RoomAplicadaIITheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

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

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val tipoRepository = TipoTecRepository(tecnicoDb.tipoTecDao())
        val servicioRepository = ServicioTecRepository(tecnicoDb.servicioTecDao())
        enableEdgeToEdge()
        setContent {
            RoomAplicadaIITheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.TecnicoList
                ) {

                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, tipoRepository,0) },
                            onDeleteTecnido = { tecnico -> deleteTecnico(tecnico) },
                            onVerTecnico =  {
                            navController.navigate(Screen.Tecnico(it.tecnicoId ?:0))
                                            },
                            onAddTecnico = {navController.navigate(Screen.Tecnico(0))
                            },
                            navController =navController
                            )
                    }
                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(viewModel = viewModel { TecnicoViewModel(repository, tipoRepository ,args.tecnicoId) },
                            navController = navController)
                    }
                    composable<Screen.TipoTecList> {
                        TipoTecListScreen(
                            viewModel = viewModel { TipoTecViewModel(tipoRepository,0) },
                            onDeleteTipoTec = { tipoTec -> deleteTipoTec(tipoTec) },
                            onVerTipoTec =  {
                                navController.navigate(Screen.TipoTec(it.tipoId ?:0 ))
                            },
                            onAddTipoTec = {navController.navigate(Screen.TipoTec(0))
                            },
                            navController =navController
                        )
                    }
                    composable<Screen.TipoTec> {
                        val args = it.toRoute<Screen.TipoTec>()
                        TipoTecScreen(viewModel = viewModel { TipoTecViewModel(tipoRepository ,args.tipoId) },
                            navController = navController)
                    }
                    composable<Screen.ServicioTecList>{
                        ServicioTecListScreen(
                            viewModel = viewModel { ServicioTecViewModel(servicioRepository, repository, 0)},
                            onVerServicioTec = {navController.navigate(Screen.ServicioTec(it.servicioId ?:0))},
                            onAddServicioTec = { navController.navigate(Screen.ServicioTec(0)) },
                            onDeleteServicioTec = {servicioTec -> deleteServicioTec(servicioTec)},
                            navController = navController
                        )
                    }
                    composable<Screen.ServicioTec> {
                        val args = it.toRoute<Screen.ServicioTec>()
                        ServicioTecScreen(viewModel = viewModel { ServicioTecViewModel(servicioRepository, repository ,args.servicioId) },
                            navController = navController)
                    }
                }
            }
        }
        fetchTecnicos()
        fetchTipoTec()
        fetchServicioTec()
    }


    private fun fetchTecnicos() {
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tecnicoDao().getAll().collect {
                tecnicos = it
            }
        }
    }
    private fun fetchTipoTec() {
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tipoTecDao().getAll().collect {
                tipoTec = it
            }
        }
    }
    private fun fetchServicioTec() {
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.servicioTecDao().getAll().collect {
                servicioTec = it
            }
        }
    }

    private var tecnicos by mutableStateOf<List<TecnicoEntity>>(emptyList())
    private var tipoTec by mutableStateOf<List<TipoTecEntity>>(emptyList())
    private var servicioTec by mutableStateOf<List<ServicioTecEntity>>(emptyList())

    private fun deleteTecnico(tecnico: TecnicoEntity) {
        val tecnicoId = tecnico.tecnicoId ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tecnicoDao().deleteById(tecnicoId)
        }
    }
    private fun deleteTipoTec(tipoTec: TipoTecEntity) {
        val tipoId = tipoTec.tipoId ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.tipoTecDao().deleteById(tipoId)
        }
    }
    private fun deleteServicioTec(servicioTec: ServicioTecEntity) {
        val servicioId = servicioTec.servicioId ?: return
        lifecycleScope.launch(Dispatchers.IO) {
            tecnicoDb.servicioTecDao().deleteById(servicioId)
        }
    }

}
sealed class Screen {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    object TipoTecList : Screen()
    @Serializable
    data class TipoTec(val tipoId: Int) : Screen()

    @Serializable
    object ServicioTecList : Screen()
    @Serializable
    data class ServicioTec(val servicioId: Int) : Screen()
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    RoomAplicadaIITheme {

    }
}
