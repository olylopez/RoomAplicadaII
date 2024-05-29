package edu.ucne.roomaplicadaii.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucne.roomaplicadaii.Screen
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var badgeCount: Double? = null
)
@Composable
fun NavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavigationItem(
            title = "Técnicos",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
        NavigationItem(
            title = "Tipos de Técnicos",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItem(
            title = "Servicios de Tecnicos",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        )
    )
    val selectedItem = remember { mutableStateOf(items[0].title) } // Inicializa el primer elemento como seleccionado

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = if (item.title == selectedItem.value) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        selected = item.title == selectedItem.value,
                        onClick = {
                            selectedItem.value = item.title
                            scope.launch { drawerState.close() }
                            when (item.title) {
                                "Técnicos" -> navController.navigate(Screen.TecnicoList)
                                "Tipos de Técnicos" -> navController.navigate(Screen.TipoTecList)
                                "Servicios de Tecnicos" -> navController.navigate(Screen.ServicioTecList)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState
    ){
        content()
    }
}