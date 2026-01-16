package dev.samuuu.booktrackcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@Composable
fun GenreListScreen(
    onBookClick: (Int) -> Unit,
    viewModel: LibroViewModel
) {
    val librosPorGenero by viewModel.librosPorGenero.collectAsState()

    if (librosPorGenero.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(librosPorGenero.keys.toList()) { genero ->
                GenreRow(genero = genero, libros = librosPorGenero[genero].orEmpty(), onBookClick = onBookClick)
            }
        }
    }
}

@Composable
fun GenreRow(
    genero: Genero,
    libros: List<dev.samuuu.booktrackcompose.model.Libro>,
    onBookClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = genero.name.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() },
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(libros) { libro ->
                // We can reuse the same item composable from BookListScreen
                Box(modifier = Modifier.width(120.dp)) { // Set a fixed width for items in the row
                     LibroPortadaItem(libro = libro, onBookClick = onBookClick)
                }
            }
        }
    }
}
