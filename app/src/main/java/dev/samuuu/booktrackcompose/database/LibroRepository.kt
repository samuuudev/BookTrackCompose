package dev.samuuu.booktrackcompose.database

import android.util.Log
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu. booktrackcompose.model. Libro

class LibroRepository(
    private val remote: LibroRemoteDataSource
) {
    companion object {
        private const val TAG = "LibroRepository"
    }

    suspend fun obtenerLibros(): List<Libro> {
        Log.d(TAG, "🔄 Repository: Solicitando todos los libros...")

        return try {
            val libros = remote.listarLibros()
            Log.d(TAG, "✅ Repository:  Recibidos ${libros.size} libros")
            libros
        } catch (e:  Exception) {
            Log.e(TAG, "❌ Repository: Error al obtener libros:  ${e.message}")
            throw e
        }
    }

    suspend fun obtenerLibrosPorGenero(genero: Genero): List<Libro> {
        Log.d(TAG, "🔄 Repository: Solicitando libros del género $genero...")

        return try {
            val libros = remote.listarLibrosPorGenero(genero)
            Log.d(TAG, "✅ Repository: Recibidos ${libros.size} libros del género $genero")
            libros
        } catch (e: Exception) {
            Log.e(TAG, "❌ Repository: Error al obtener libros por género: ${e.message}")
            throw e
        }
    }

    suspend fun crearLibro(libro: Libro): Libro {
        Log.d(TAG, "🔄 Repository: Creando libro '${libro.titulo}'...")

        return try {
            val libroCreado = remote.agregarLibro(libro)
            Log.d(TAG, "✅ Repository: Libro creado con ID: ${libroCreado.id}")
            libroCreado
        } catch (e: Exception) {
            Log.e(TAG, "❌ Repository: Error al crear libro: ${e.message}")
            throw e
        }
    }
}