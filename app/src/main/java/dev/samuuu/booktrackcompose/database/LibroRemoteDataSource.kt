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

    suspend fun listarLibros(): List<Libro> {
        Log.d(TAG, "🚀 Iniciando petición para listar todos los libros...")

        return try {
            Log.d(TAG, "📡 Conectando con Supabase...")

            val resultado = supabase.from("libro").select()
            Log.d(TAG, "✅ Respuesta recibida de Supabase")

            val libros = resultado.decodeList<Libro>()
            Log.d(TAG, "📚 Libros obtenidos: ${libros.size}")

            libros.forEach { libro ->
                Log. d(TAG, "   → Libro: ${libro.titulo} | Autor: ${libro.autor} | Género: ${libro.genero}")
            }

            libros
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al listar libros: ${e.message}")
            Log.e(TAG, "❌ Tipo de error: ${e.javaClass.simpleName}")
            Log.e(TAG, "❌ Stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }

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
            Log.e(TAG, "❌ Error al listar libros por género: ${e.message}")
            Log.e(TAG, "❌ Stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }

    suspend fun agregarLibro(libro: Libro): Libro {
        Log.d(TAG, "🚀 Iniciando petición para agregar libro: ${libro.titulo}")
        Log.d(TAG, "📋 Datos del libro:")
        Log.d(TAG, "   → Título: ${libro.titulo}")
        Log.d(TAG, "   → Autor: ${libro.autor}")
        Log.d(TAG, "   → Género: ${libro.genero}")
        Log.d(TAG, "   → Valoración: ${libro.valoracion}")
        Log.d(TAG, "   → Estado: ${libro.estado}")
        Log.d(TAG, "   → ImagenUrl: ${libro.imagenUrl}")

        return try {
            val resultado = supabase.from("libro").insert(libro) {
                select()
            }. decodeSingle<Libro>()

            Log.d(TAG, "✅ Libro agregado correctamente con ID: ${resultado.id}")
            resultado
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al agregar libro: ${e.message}")
            Log.e(TAG, "❌ Stack trace: ${e.stackTraceToString()}")
            throw e
        }
    }
}