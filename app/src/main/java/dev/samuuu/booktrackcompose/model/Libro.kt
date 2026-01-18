package dev.samuuu.booktrackcompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Libro(

    @SerialName("id")
    val id: Int? = null, // Nullable ya que la base de datos debe generarlo
    @SerialName("created_at")
    val createdAt: String? = null, // Nullable por que la base de datos lo genera
    @SerialName("titulo")
    val titulo: String,
    @SerialName("autor")
    val autor: String,
    @SerialName("valoracion")
    val valoracion: Int,
    @SerialName("imagenUrl")
    val imagenUrl: String,
    @SerialName("genero")
    val genero: Genero,

)
