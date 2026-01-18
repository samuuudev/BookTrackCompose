# 📚 BookTrack Compose (Material 3)

Aplicación móvil para el **seguimiento de lectura de libros**, desarrollada con **Jetpack Compose** y **Material 3**.

El objetivo de la app es permitir al usuario gestionar su biblioteca personal, marcar el estado de lectura de cada libro y valorarlos una vez finalizados.
Incluyendo el aprendizaje sobre el desarrollo de aplicaciones moviles usando **Android Studio**

---

## 🚧 Estado del proyecto

**En desarrollo activo**

Este proyecto es una **reescritura/migración** de una versión anterior desarrollada con **Material 2**.
Actualmente no todas las funcionalidades están finalizadas ni estabilizadas.

---

## ✨ Funcionalidades principales

- Catálogo de libros
- Añadir libros manualmente al catálogo
- Selección del libro en lectura
- Estados de lectura:
  - Pendiente
  - Leído
  - Ninguno
- Valoración de libros leídos
  - Valoración de 1 a 5 estrellas


ARREGLAR - Filtrado de libros por género


## Informacion de la app
- SupaBaseClient.kt: Configura el cliente de Supabase con la URL y la clave de API.
- Libro.kt: Define el modelo de datos para un libro.
- LibroRemoteDataSource.kt: Maneja la comunicación con Supabase para obtener y agregar libros. (Es mi provider)
- LibroRepository.kt: Actúa como intermediario entre la fuente de datos remota y el viewmodel. Ademas hace de control de excepciones que pueda ocasionar el remoteDataSource.
- LibroViewModel.kt: Gestiona la lógica y los datos para las pantallas
- LibroViewModelFactory.kt: Crea instancias de LibroViewModel con dependencias.
- LibrosScreen.kt: Pantalla principal que muestra la lista de libros.
- LibrosPorGeneroScreen.kt: Pantalla que muestra libros filtrados por género.
- AgregarLibroScreen.kt: Pantalla para agregar nuevos libros al catálogo.
- AppNavigation.kt: Configura la navegación entre las diferentes pantallas de la app desacoplando codigwo en MainActivity.
- MainActivity.kt: Punto de entrada de la aplicación que configura la navegación.

Hago un control de excepciones en el remoteDataSource, LibroRepository y LibroViewModel para asegurar que cada capa me aporte informacion en caso de error detectar donde falla, sabiendo que son innecesarios y con la opcion de delegar excepciones a la capa superior.