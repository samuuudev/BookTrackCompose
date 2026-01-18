package dev.samuuu.booktrackcompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarLibroScreen(
    viewModel: LibroViewModel,
    onLibroAgregado: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var valoracion by remember { mutableIntStateOf(3) }
    var estado by remember { mutableStateOf("Por leer") }
    var generoSeleccionado by remember { mutableStateOf(Genero.FICCION) } // uso como valor por defecto FICCION usando mi enum
    var generoExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }
    var favoritoSeleccionado by remember { mutableStateOf(false) }
    var pendienteSeleccionado by remember { mutableStateOf(false) }

    val estados = listOf("Por leer", "Leyendo", "Leído", "Abandonado")
    val uiState by viewModel.uiState.collectAsState()

    // Navegar cuando se añade correctamente
    LaunchedEffect(uiState. mensaje) {
        if (uiState.mensaje == "Libro añadido correctamente") {
            viewModel.limpiarMensaje()
            onLibroAgregado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = autor,
                onValueChange = { autor = it },
                label = { Text("Autor *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = imagenUrl,
                onValueChange = { imagenUrl = it },
                label = { Text("URL de la imagen") },
                placeholder = { Text("https://ejemplo.com/imagen.jpg") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Selector de género
            ExposedDropdownMenuBox(
                expanded = generoExpanded,
                onExpandedChange = { generoExpanded = it }
            ) {
                OutlinedTextField(
                    value = generoSeleccionado.name. replace("_", " "),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = generoExpanded,
                    onDismissRequest = { generoExpanded = false }
                ) {
                    Genero.entries.forEach { genero ->
                        DropdownMenuItem(
                            text = {
                                Text(getGeneroEmoji(genero) + " " + genero.name.replace("_", " "))
                            },
                            onClick = {
                                generoSeleccionado = genero
                                generoExpanded = false
                            }
                        )
                    }
                }
            }

            // Valoración con estrellas
            Column {
                Text(
                    text = "Valoración",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(5) { index ->
                        FilterChip(
                            selected = index < valoracion,
                            onClick = { valoracion = index + 1 },
                            label = { Text("⭐") }
                        )
                    }
                }
                Text(
                    text = "$valoracion de 5 estrellas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier. height(8.dp))

            Button(
                onClick = {
                    if (titulo.isNotBlank() && autor.isNotBlank()) {
                        val nuevoLibro = Libro(
                            titulo = titulo. trim(),
                            autor = autor.trim(),
                            imagenUrl = imagenUrl.ifBlank {
                                "https://via.placeholder.com/150x200? text=${titulo.take(10)}"
                            },
                            valoracion = valoracion,
                            genero = generoSeleccionado
                        )
                        viewModel.agregarLibro(nuevoLibro)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = titulo.isNotBlank() && autor.isNotBlank() && ! uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier. size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("📚 Guardar Libro", style = MaterialTheme.typography. titleMedium)
                }
            }

            // Mostrar error si existe
            uiState.mensaje?.let { mensaje ->
                if (mensaje.startsWith("Error")) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = mensaje,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

private fun getEstadoEmoji(estado: String): String {
    return when (estado) {
        "Por leer" -> "📋"
        "Leyendo" -> "📖"
        "Leído" -> "✅"
        "Abandonado" -> "❌"
        else -> "📚"
    }
}