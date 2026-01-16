package dev.samuuu.booktrackcompose.model

import kotlinx.serialization.Serializable

@Serializable
enum class Genero {
    FICCION,
    NO_FICCION,
    CIENCIA_FICCION,
    FANTASIA,
    MISTERIO,
    ROMANCE,
    TERROR,
    BIOGRAFIA,
    HISTORIA,
    INFANTIL,
    OTRO
}