package dev.samuuu.booktrackcompose.database

import dev.samuuu.booktrackcompose.model.Libro
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibroRepository(private val postgrest: Postgrest) {

    suspend fun getAllLibros(): List<Libro> {
        return withContext(Dispatchers.IO) {
            try {
                val result = postgrest.from("libro").select().decodeList<Libro>()
                result
            } catch (e: Exception) {
                // In a real app, you should handle this error more gracefully
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    suspend fun addLibro(libro: Libro) {
        withContext(Dispatchers.IO) {
            try {
                postgrest.from("libro").insert(libro)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}