package dev.samuuu.booktrackcompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.model.Libro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroScreen(
    libro: Libro
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de la portada del libro
                AsyncImage(
                    model = libro.imagenUrl,
                    contentDescription = "Portada del libro",
                    modifier = Modifier
                        .size(150.dp, 200.dp) // Tamaño de la portada
                )

                // Título del libro
                Text(
                    text = "Título: ${libro.titulo}",
                    style = MaterialTheme.typography.titleLarge
                )

                // Autor del libro
                Text(
                    text = "Autor: ${libro.autor}",
                    style = MaterialTheme.typography.bodyLarge
                )

                // Género del libro
                Text(
                    text = "Género: ${libro.genero.name.replace("_", " ")}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}