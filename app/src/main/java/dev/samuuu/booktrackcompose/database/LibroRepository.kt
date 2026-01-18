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
        Log.d(TAG, "Solicitando todos los libros...")

        return try {
            val libros = remote.listarLibros()
            Log.d(TAG, "Recibidos ${libros.size} libros")
            libros
        } catch (e:  Exception) {
            Log.e(TAG, "Error al obtener libros:  ${e.message}")
            throw e
        }
    }

    suspend fun obtenerLibrosPorGenero(genero: Genero): List<Libro> {
        Log.d(TAG, "Solicitando libros del género $genero...")

        return try {
            val libros = remote.listarLibrosPorGenero(genero)
            // Activar por si se necesita saber cuantos libros de genero recibe de la bbdd
            // Log.d(TAG, "Recibidos ${libros.size} libros del género $genero")
            libros
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener libros por género: ${e.message}")
            throw e
        }
    }

    suspend fun crearLibro(libro: Libro): Libro {
        Log.d(TAG, "Creando libro '${libro.titulo}'...")

        return try {
            val libroCreado = remote.agregarLibro(libro)
            Log.d(TAG, "Libro creado con ID: ${libroCreado.id}")
            libroCreado
        } catch (e: Exception) {
            Log.e(TAG, "Error al crear libro: ${e.message}")
            throw e
        }
    }

    /**
     * Actualiza el estado de un libro específico.
     *
     * @param libroId El ID del libro a actualizar.
     * @param estado El nuevo estado del libro.
     * @return El libro actualizado.
     * @throws Exception Si ocurre un error durante la actualización.
     */
    suspend fun actualizarEstadoLibro(libroId: Int, estado: String): Libro {
        return try{
            val libroActualizado = remote.actualizarEstadoLibro(libroId, estado)
            Log.d(TAG, "Estado del libro $libroId actualizado a $estado")
            libroActualizado
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar estado del libro: ${e.message}")
            throw e
        }
    }

    suspend fun actualizarFavoritoLibro(libroId: Int, favorito: Boolean): Libro {
        return try {
            val libroActualizado = remote.actualizarFavoritoLibro(libroId, favorito)
            Log.d(TAG, "Favorito del libro $libroId actualizado a $favorito")
            libroActualizado
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar favorito del libro: ${e.message}")
            throw e
        }
    }

    suspend fun actualizarPendienteLibro(libroId: Int, pendiente: Boolean): Libro {
        return try {
            val libroActualizado = remote.actualizarPendienteLibro(libroId, pendiente)
            Log.d(TAG, "Campo 'pendiente' del libro $libroId actualizado a $pendiente")
            libroActualizado
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar el campo 'pendiente' del libro: ${e.message}")
            throw e
        }
    }
}