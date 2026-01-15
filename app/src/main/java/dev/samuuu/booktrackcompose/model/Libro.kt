package dev.samuuu.booktrackcompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libro")
data class Libro(
    val id: String,
    val titulo: String,
    val autor: String,
    val estado: EstadosLibro,
    val valoracion: Int?,
    val portadaUrl: String?,
    val createdAt: String
)
