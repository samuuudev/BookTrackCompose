package dev.samuuu.booktrackcompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.samuuu.booktrackcompose.database.LibroRepository
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibroViewModel(private val repository: LibroRepository) : ViewModel() {

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros.asStateFlow()

    private val _librosPorGenero = MutableStateFlow<Map<Genero, List<Libro>>>(emptyMap())
    val librosPorGenero: StateFlow<Map<Genero, List<Libro>>> = _librosPorGenero.asStateFlow()

    init {
        cargarLibros()
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            val listaLibros = repository.getAllLibros()
            _libros.value = listaLibros
            _librosPorGenero.value = listaLibros.groupBy { it.genero }
        }
    }

    fun addLibro(libro: Libro) {
        viewModelScope.launch {
            repository.addLibro(libro)
            // Refresh the list after adding a new book
            cargarLibros()
        }
    }
}

// Factory to create an instance of the ViewModel with the repository
class LibroViewModelFactory(private val repository: LibroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}