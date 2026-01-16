package dev.samuuu.booktrackcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@Composable
fun BookDetailScreen(
    bookId: Int,
    viewModel: LibroViewModel
) {
    val libros by viewModel.libros.collectAsState()
    // Find the book in the list. This is efficient as the list is already in memory.
    val libro = libros.find { it.id == bookId }

    if (libro == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = libro.imagenUrl,
                contentDescription = "Portada de ${libro.titulo}",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(300.dp)
            )

            Text(libro.titulo, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Text("por ${libro.autor}", style = MaterialTheme.typography.titleMedium)

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            DetailRow("Género:", libro.genero.name)
            DetailRow("Estado:", libro.estado)
            DetailRow("Valoración:", "${libro.valoracion} / 5")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(value, modifier = Modifier.weight(2f))
    }
}
