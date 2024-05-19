package edu.ucne.roomaplicadaii.navigation

sealed class Screen(val route: String) {
    object TecnicoList : Screen("tecnico_list")
    object Tecnico : Screen("tecnico/{tecnicoId}") {
        fun createRoute(tecnicoId: Int) = "tecnico/$tecnicoId"
    }
}