package dev.samuuu.booktrackcompose.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.samuuu.booktrackcompose.database.LibroRepository
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LibrosUiState(
    val libros: List<Libro> = emptyList(),
    val librosPorGenero: Map<Genero, List<Libro>> = emptyMap(),
    val isLoading: Boolean = false,
    val mensaje: String?  = null
)

class LibroViewModel(
    private val repository:  LibroRepository
) : ViewModel() {

    companion object {
        private const val TAG = "LibroViewModel"
    }

    private val _uiState = MutableStateFlow(LibrosUiState())
    val uiState: StateFlow<LibrosUiState> = _uiState.asStateFlow()

    init {
        Log. d(TAG, "🎬 ViewModel inicializado")
    }

    fun cargarLibros() {
        Log.d(TAG, "📖 cargarLibros() llamado")

        viewModelScope.launch {
            Log.d(TAG, "🔄 Iniciando carga de libros en coroutine...")
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log.d(TAG, "⏳ Esperando respuesta del repository...")
                val libros = repository.obtenerLibros()

                Log. d(TAG, "✅ Libros recibidos en ViewModel: ${libros.size}")
                libros.forEachIndexed { index, libro ->
                    Log.d(TAG, "   [$index] ${libro.titulo} - ${libro.autor}")
                }

                _uiState. value = _uiState.value.copy(
                    libros = libros,
                    isLoading = false,
                    mensaje = null
                )
                Log.d(TAG, "✅ Estado actualizado con ${libros.size} libros")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en cargarLibros(): ${e.message}")
                Log.e(TAG, "❌ Tipo:  ${e.javaClass.simpleName}")
                Log.e(TAG, "❌ Causa: ${e.cause?. message}")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mensaje = "Error al cargar libros:  ${e.message}"
                )
            }
        }
    }

    fun cargarLibrosAgrupadosPorGenero() {
        Log.d(TAG, "📖 cargarLibrosAgrupadosPorGenero() llamado")

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log. d(TAG, "⏳ Obteniendo libros para agrupar...")
                val libros = repository. obtenerLibros()

                Log. d(TAG, "📊 Agrupando ${libros.size} libros por género...")
                val agrupadosPorGenero = libros.groupBy { it.genero }

                agrupadosPorGenero. forEach { (genero, listaLibros) ->
                    Log.d(TAG, "   → $genero: ${listaLibros.size} libros")
                }

                _uiState.value = _uiState.value.copy(
                    librosPorGenero = agrupadosPorGenero,
                    isLoading = false,
                    mensaje = null
                )
                Log.d(TAG, "✅ Estado actualizado con libros agrupados")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en cargarLibrosAgrupadosPorGenero(): ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mensaje = "Error al cargar libros: ${e. message}"
                )
            }
        }
    }

    fun agregarLibro(libro:  Libro) {
        Log.d(TAG, "➕ agregarLibro() llamado para: ${libro.titulo}")

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log. d(TAG, "⏳ Enviando libro al repository...")
                repository.crearLibro(libro)

                Log.d(TAG, "✅ Libro creado, recargando lista...")
                cargarLibros()

                _uiState.value = _uiState.value.copy(
                    mensaje = "Libro añadido correctamente"
                )
                Log. d(TAG, "✅ Mensaje de éxito establecido")

            } catch (e: Exception) {
                Log.e(TAG, "❌ Error en agregarLibro(): ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mensaje = "Error al añadir libro: ${e.message}"
                )
            }
        }
    }

    fun limpiarMensaje() {
        Log.d(TAG, "🧹 Limpiando mensaje")
        _uiState.value = _uiState.value.copy(mensaje = null)
    }
}