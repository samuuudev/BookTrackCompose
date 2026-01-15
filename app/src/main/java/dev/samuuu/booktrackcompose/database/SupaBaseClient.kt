package dev.samuuu.booktrackcompose.database

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.postgrest.Postgrest

object SupaBaseClient {
    private const val SUPABASE_URL = "https://cwbuhkkiiwuvgjovfzwe.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_8EoepQ3JLhhZ_3TSUSqBng_jAuXvwiZ"

    val client = createSupabaseClient(

        supabaseUrl = SUPABASE_URL,

        supabaseKey = SUPABASE_KEY
    ){
        install(Storage)
        install(Postgrest)
    }
}








