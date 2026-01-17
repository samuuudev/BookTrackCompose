package dev.samuuu.booktrackcompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.samuuu.booktrackcompose.screens.AgregarLibroScreen
import dev.samuuu.booktrackcompose.screens.LibrosPorGeneroScreen
import dev.samuuu.booktrackcompose.screens.LibrosScreen
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel
import dev.samuuu.booktrackcompose.viewModel.LibroViewModelFactory

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon:  ImageVector
) {
    object Libros : Screen(
        route = "libros",
        title = "Inicio",
        selectedIcon = Icons. Filled.Home,
        unselectedIcon = Icons. Outlined.Home
    )
    object LibrosPorGenero :  Screen(
        route = "libros_por_genero",
        title = "Géneros",
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons. Outlined.List
    )
    object AgregarLibro : Screen(
        route = "agregar_libro",
        title = "Añadir",
        selectedIcon = Icons. Filled.Add,
        unselectedIcon = Icons.Outlined. Add
    )
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: LibroViewModel = viewModel(factory = LibroViewModelFactory())

    val screens = listOf(
        Screen. Libros,
        Screen.LibrosPorGenero,
        Screen.AgregarLibro
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?. any {
                        it.route == screen.route
                    } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Libros.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Libros.route) {
                LibrosScreen(viewModel = viewModel)
            }

            composable(Screen.LibrosPorGenero. route) {
                LibrosPorGeneroScreen(viewModel = viewModel)
            }

            composable(Screen.AgregarLibro.route) {
                AgregarLibroScreen(
                    viewModel = viewModel,
                    onLibroAgregado = {
                        navController.navigate(Screen.Libros.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}