package dev.samuuu.booktrackcompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.model.Libro
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@Composable
fun BookListScreen(
    onBookClick: (Int) -> Unit, // Callback to navigate to detail screen
    viewModel: LibroViewModel
) {
    val libros by viewModel.libros.collectAsState()

    if (libros.isEmpty()) {
        // Show a loading indicator while the books are being fetched
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // We can adjust the number of columns
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(libros) { libro ->
                LibroPortadaItem(libro = libro, onBookClick = onBookClick)
            }
        }
    }
}

@Composable
fun LibroPortadaItem(libro: Libro, onBookClick: (Int) -> Unit) {
    AsyncImage(
        model = libro.imagenUrl,
        contentDescription = "Portada de ${libro.titulo}",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(2f / 3f) // Typical book cover aspect ratio
            .clickable { libro.id?.let { onBookClick(it) } }
    )
}
