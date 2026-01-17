package dev.samuuu.booktrackcompose.database

import android.util.Log
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupaBaseClient {
    private const val TAG = "SupaBaseClient"

    // ⚠️ IMPORTANTE: Cambia estos valores por los tuyos
    private const val SUPABASE_URL = "https://cwbuhkkiiwuvgjovfzwe.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_8EoepQ3JLhhZ_3TSUSqBng_jAuXvwiZ" // ← Usa tu clave anon/public

    val client by lazy {
        Log.d(TAG, "🔧 Inicializando cliente Supabase...")
        Log.d(TAG, "🌐 URL: $SUPABASE_URL")
        Log.d(TAG, "🔑 Key (primeros 20 chars): ${SUPABASE_KEY.take(20)}...")

        try {
            val supabaseClient = createSupabaseClient(
                supabaseUrl = SUPABASE_URL,
                supabaseKey = SUPABASE_KEY
            ) {
                install(Storage)
                install(Postgrest)
            }
            Log.d(TAG, "✅ Cliente Supabase inicializado correctamente")
            supabaseClient
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al inicializar Supabase: ${e.message}")
            throw e
        }
    }
}