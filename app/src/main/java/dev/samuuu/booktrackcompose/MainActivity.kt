package dev.samuuu.booktrackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.samuuu.booktrackcompose.database.LibroRepository
import dev.samuuu.booktrackcompose.database.SupaBaseClient
import dev.samuuu.booktrackcompose.ui.screens.AddBookScreen
import dev.samuuu.booktrackcompose.ui.screens.BookDetailScreen
import dev.samuuu.booktrackcompose.ui.screens.BookListScreen
import dev.samuuu.booktrackcompose.ui.screens.GenreListScreen
import dev.samuuu.booktrackcompose.ui.theme.BooktrackComposeTheme
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel
import dev.samuuu.booktrackcompose.viewModel.LibroViewModelFactory
import io.github.jan.supabase.postgrest.postgrest

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object BookList : Screen("bookList", "Todos", Icons.Default.List)
    object GenreList : Screen("genreList", "Géneros", Icons.Default.List)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BooktrackComposeTheme {
                val libroRepository = LibroRepository(SupaBaseClient.client.postgrest)
                val libroViewModel: LibroViewModel = viewModel(factory = LibroViewModelFactory(libroRepository))

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val bottomNavItems = listOf(Screen.BookList, Screen.GenreList)

                Scaffold(
                    bottomBar = {
                        if (currentRoute in bottomNavItems.map { it.route }) {
                            NavigationBar {
                                bottomNavItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                                        label = { Text(screen.label) },
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        if (currentRoute in bottomNavItems.map { it.route }) {
                            FloatingActionButton(onClick = { navController.navigate("addBook") }) {
                                Icon(Icons.Default.Add, contentDescription = "Añadir libro")
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.BookList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.BookList.route) {
                            BookListScreen(
                                onBookClick = { bookId -> navController.navigate("bookDetail/$bookId") },
                                viewModel = libroViewModel
                            )
                        }
                        composable(Screen.GenreList.route) {
                            GenreListScreen(
                                onBookClick = { bookId -> navController.navigate("bookDetail/$bookId") },
                                viewModel = libroViewModel
                            )
                        }
                        composable(
                            route = "bookDetail/{bookId}",
                            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
                            BookDetailScreen(bookId = bookId, viewModel = libroViewModel)
                        }
                        composable("addBook") {
                            AddBookScreen(
                                onBookAdded = { navController.popBackStack() },
                                viewModel = libroViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
