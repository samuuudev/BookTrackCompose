package dev.samuuu.booktrackcompose.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.samuuu.booktrackcompose.database.SupaBaseClient
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import dev.samuuu.booktrackcompose.viewModel.LibroViewModel
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    onBookAdded: () -> Unit,
    viewModel: LibroViewModel
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf(3f) }
    var estado by remember { mutableStateOf("PENDIENTE") }
    var genero by remember { mutableStateOf(Genero.FICCION) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Añadir Nuevo Libro", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
        
        // Image Picker
        AsyncImage(model = imageUri, contentDescription = "Portada", modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
        Button(onClick = { imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text("Seleccionar Portada")
        }

        // Genre Dropdown
        ExposedDropdownMenu("Género", Genero.entries.map { it.name }) { selected -> genero = Genero.valueOf(selected) }

        // Status Dropdown (using simple strings for now)
        ExposedDropdownMenu("Estado", listOf("LEIDO", "PENDIENTE", "EN_PROGRESO")) { selected -> estado = selected }
        
        // Rating Slider
        Text("Valoración: ${valoracion.roundToInt()} / 5")
        Slider(value = valoracion, onValueChange = { valoracion = it }, valueRange = 1f..5f, steps = 3)

        Button(
            onClick = {
                coroutineScope.launch {
                    isSaving = true
                    var finalImageUrl = ""
                    imageUri?.let { uri ->
                        val imageBytes = context.contentResolver.openInputStream(uri)?.readBytes()
                        imageBytes?.let {
                            val path = "portadas/${UUID.randomUUID()}"
                            SupaBaseClient.client.storage.from("portadas").upload(path, it)
                            finalImageUrl = SupaBaseClient.client.storage.from("portadas").publicUrl(path)
                        }
                    }
                    
                    val newBook = Libro(
                        titulo = titulo, autor = autor, valoracion = valoracion.roundToInt(),
                        estado = estado, imagenUrl = finalImageUrl, genero = genero
                    )
                    
                    viewModel.addLibro(newBook)
                    isSaving = false
                    onBookAdded()
                }
            },
            enabled = titulo.isNotBlank() && autor.isNotBlank() && imageUri != null && !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSaving) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Guardar Libro")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(label: String, items: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedItem = item
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}