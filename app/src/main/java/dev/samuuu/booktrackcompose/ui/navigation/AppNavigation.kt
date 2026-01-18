package dev.samuuu.booktrackcompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.samuuu.booktrackcompose.screens.AgregarLibroScreen
import dev.samuuu.booktrackcompose.screens.LibrosPorGeneroScreen
import dev.samuuu.booktrackcompose.screens.LibrosScreen
import dev.samuuu.booktrackcompose.screens.DetalleLibroScreen
import dev.samuuu.booktrackcompose.screens.InfoAppScreen
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel
import dev.samuuu.booktrackcompose.viewModel.LibroViewModelFactory

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Libros : Screen(
        route = "libros",
        title = "Inicio",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object LibrosPorGenero : Screen(
        route = "libros_por_genero",
        title = "Géneros",
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List
    )

    object AgregarLibro : Screen(
        route = "agregar_libro",
        title = "Añadir",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )

    object DetalleLibro : Screen(
        route = "detalle_libro/{libroId}",
        title = "Detalle",
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Outlined.Home
    ) {
        fun createRoute(libroId: Int) = "detalle_libro/$libroId"
    }

    object InfoApp : Screen(
        route = "info",
        title = "Info",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info
    )
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: LibroViewModel = viewModel(factory = LibroViewModelFactory())

    // Mi lista de pantallas para la navegacion
    val screens = listOf(
        Screen.Libros,
        Screen.LibrosPorGenero,
        Screen.AgregarLibro,
        Screen.InfoApp
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
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
                LibrosScreen(
                    viewModel = viewModel,
                    onLibroClick = { libro ->
                        val libroId = libro.id
                        if (libroId != null) {
                            navController.navigate(Screen.DetalleLibro.createRoute(libroId))
                        }
                    }
                )
            }

            composable(Screen.LibrosPorGenero.route) {
                LibrosPorGeneroScreen(viewModel = viewModel) { libro ->
                    val libroId = libro.id
                    if (libroId != null) {
                        navController.navigate(Screen.DetalleLibro.createRoute(libroId))
                    }
                }
            }

            composable(Screen.AgregarLibro.route) {
                AgregarLibroScreen(viewModel = viewModel) {
                    navController.navigate(Screen.Libros.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            composable(Screen.InfoApp.route) {
                InfoAppScreen()
            }

            composable(
                route = Screen.DetalleLibro.route,
                arguments = listOf(navArgument("libroId") { type = NavType.IntType })
            ) {
                val libroId = it.arguments?.getInt("libroId")
                val libro = libroId?.let { id -> viewModel.getLibroById(id) }

                if (libro != null) {
                    DetalleLibroScreen(libro = libro)
                }
            }
        }
    }
}