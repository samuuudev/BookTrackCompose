package dev.samuuu.booktrackcompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrosPorGeneroScreen(viewModel: LibroViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarLibrosAgrupadosPorGenero()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏷️ Por Género") },
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
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState. librosPorGenero.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏷️",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay libros por género",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        uiState.librosPorGenero.forEach { (genero, libros) ->
                            item(key = genero.name) {
                                GeneroSection(genero = genero, libros = libros)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GeneroSection(genero: Genero, libros: List<Libro>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getGeneroEmoji(genero) + " " + genero.name.replace("_", " "),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Badge {
                Text("${libros.size}")
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(libros, key = { it.id ?: it.hashCode() }) { libro ->
                LibroCardHorizontal(libro = libro)
            }
        }
    }
}

@Composable
fun LibroCardHorizontal(libro: Libro) {
    Card(
        modifier = Modifier.width(140.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = libro.imagenUrl,
                contentDescription = libro.titulo,
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = libro.titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = libro.autor,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow. Ellipsis
            )
            Row {
                repeat(libro.valoracion) {
                    Text("⭐", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

fun getGeneroEmoji(genero: Genero): String {
    return when (genero) {
        Genero.FICCION -> "📖"
        Genero.NO_FICCION -> "📰"
        Genero.CIENCIA_FICCION -> "🚀"
        Genero.FANTASIA -> "🧙"
        Genero.MISTERIO -> "🔍"
        Genero.ROMANCE -> "💕"
        Genero.TERROR -> "👻"
        Genero.BIOGRAFIA -> "👤"
        Genero.HISTORIA -> "🏛️"
        Genero.INFANTIL -> "🧒"
        Genero.OTRO -> "📚"
    }
}