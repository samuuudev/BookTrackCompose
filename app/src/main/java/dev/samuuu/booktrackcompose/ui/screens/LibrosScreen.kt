package dev.samuuu.booktrackcompose.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.model.Libro
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrosScreen(viewModel: LibroViewModel, onLibroClick: (Libro) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Log.d("LibrosScreen", "uiState: $uiState")

    LaunchedEffect(Unit) {
        viewModel.cargarLibros()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📚 Mis Libros") },
                colors = TopAppBarDefaults.topAppBarColors(
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
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.libros.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📖",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay libros todavía",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "¡Añade tu primer libro!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.libros, key = { it.id ?: it.hashCode() }) { libro ->
                            LibroCard(libro = libro, onClick = onLibroClick)
                        }
                    }
                }
            }

            // Snackbar para mensajes
            uiState.mensaje?.let { mensaje ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.limpiarMensaje() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(mensaje)
                }
            }
        }
    }
}

@Composable
fun LibroCard(libro: Libro, onClick: (Libro) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(libro) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Imagen de la portada del libro
            AsyncImage(
                model = libro.imagenUrl ?: "",
                contentDescription = libro.titulo,
                modifier = Modifier
                    .size(80.dp, 120.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información básica del libro
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = libro.autor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Género del libro (sin estado ni pendiente)
                AssistChip(
                    onClick = { },
                    label = { Text(libro.genero.name.replace("_", " ")) }
                )
            }
        }
    }
}