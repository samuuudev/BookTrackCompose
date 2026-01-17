package dev.samuuu.booktrackcompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuuu.booktrackcompose.database.LibroRemoteDataSource
import dev.samuuu.booktrackcompose.database.LibroRepository
import dev.samuuu.booktrackcompose.database.SupaBaseClient

class LibroViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibroViewModel::class. java)) {
            val remoteDataSource = LibroRemoteDataSource(SupaBaseClient.client)
            val repository = LibroRepository(remoteDataSource)
            return LibroViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}