package dev.samuuu.booktrackcompose.database

import android.util.Log
import dev.samuuu.booktrackcompose.model.Genero
import dev.samuuu.booktrackcompose.model.Libro
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from


class LibroRemoteDataSource(private val supabase: SupabaseClient) {

    companion object {
        private const val TAG = "LibroRemoteDataSource"
    }

    /**
     * Lista todos los libros desde la tabla "libro" en Supabase.
     * Realiza una conexion, ejecuta la consulta y decodifica la respuesta en una lista de objetos libro
     */
    suspend fun listarLibros(): List<Libro> {
        Log.d(TAG, "Iniciando petición para listar todos los libros")

        return try {
            Log.d(TAG, "Conectando con Supabase")

            val resultado = supabase.from("libro").select()
            Log.d(TAG, "Respuesta recibida de Supabase: $resultado")

            val libros = resultado.decodeList<Libro>()
            Log.d(TAG, "Libros obtenidos: ${libros.size}")

            libros.forEach { libro ->
                // Log. d(TAG, "Libro: ${libro.titulo} | Autor: ${libro.autor} | Género: ${libro.genero}")
            }

            libros
        } catch (e: Exception) {
            Log.e(TAG, "Error al listar libros: ${e.message}")
            throw e
        }
    }

    /**
     * Lista los libros filtrados por género desde la tabla "libro" en Supabase.
     * Realiza una conexion, ejecuta la consulta con filtro y decodifica la respuesta en una lista de objetos libro
     */
    suspend fun listarLibrosPorGenero(genero: Genero): List<Libro> {
        Log. d(TAG, "🚀 Iniciando petición para listar libros por género: $genero")

        return try {
            val resultado = supabase.from("libro")
                .select {
                    filter {
                        eq("genero", genero.name)
                    }
                }

            val libros = resultado.decodeList<Libro>()
            Log.d(TAG, "📚 Libros del género $genero: ${libros.size}")

            libros
        } catch (e: Exception) {
            Log.e(TAG, "Error al listar libros por género: ${e.message}")
            throw e
        }
    }

    /**
     * Agrega un nuevo libro a la tabla "libro" en Supabase.
     * Realiza una conexion, ejecuta la inserción
     */
    suspend fun agregarLibro(libro: Libro): Libro {

        // los logs comentados los use para depurar la funcion cuando me dio problemas y me parecen utiles en ciertos casos
        // por eso se quedan comentados
        Log.d(TAG, "Iniciando petición para agregar libro: ${libro.titulo}")
//        Log.d(TAG, "Datos del libro:")
//        Log.d(TAG, "Título: ${libro.titulo}")
//        Log.d(TAG, "Autor: ${libro.autor}")
//        Log.d(TAG, "Género: ${libro.genero}")
//        Log.d(TAG, "Valoración: ${libro.valoracion}")
//        Log.d(TAG, "Estado: ${libro.estado}")
//        Log.d(TAG, "ImagenUrl: ${libro.imagenUrl}")

        return try {
            val resultado = supabase.from("libro").insert(libro) {
                select()
            }.decodeSingle<Libro>()

            Log.d(TAG, "Libro agregado correctamente con ID: ${resultado.id}")
            resultado
        } catch (e: Exception) {
            Log.e(TAG, "Error al agregar libro: ${e.message}")
            throw e
        }
    }

    /*
    *
    * Los metodos de actualizar el estado, favorito y pendiente del libro son similares
    * No se usan ya que no me acepta la clausula where, dandome fallos.
    * He probado usando filter, eq, y otras variantes pero nada funciona
    * La ia no me ha resuelto el problema
    *
    * */



    /**
     * Actualiza el estado de un libro específico.
     *
     * @param libroId El ID del libro a actualizar.
     * @param nuevoEstado El nuevo estado del libro.
     * @return El libro actualizado.
     */
    suspend fun actualizarEstadoLibro(libroId: Int, nuevoEstado: String): Libro {
        return try {
            supabase.from("libro")
                .update(mapOf("estado" to nuevoEstado)) {
                    "id" to libroId
                }
                .decodeSingle<Libro>()
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar campo 'estado' del libro: ${e.message}")
            throw e
        }
    }

    /**
     * Actualiza el estado de favorito de un libro específico.
     *
     * @param libroId El ID del libro a actualizar.
     * @param esFavorito El nuevo estado de favorito del libro.
     * @return El libro actualizado.
     */
    suspend fun actualizarFavoritoLibro(libroId: Int, esFavorito: Boolean): Libro {
        return try{ supabase.from("libro")
            .update(mapOf("favorito" to esFavorito)) {
                "id" to libroId
            }
            .decodeSingle()
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar el campo 'favorito' del libro: ${e.message}")
            throw e
        }
    }

    suspend fun actualizarPendienteLibro(libroId: Int, esPendiente: Boolean): Libro {
        val resultado = supabase.from("libro")
            .update(mapOf("pendiente" to esPendiente)) {
                "id" to libroId
            }
            .decodeSingle<Libro>()
        return resultado
    }
}