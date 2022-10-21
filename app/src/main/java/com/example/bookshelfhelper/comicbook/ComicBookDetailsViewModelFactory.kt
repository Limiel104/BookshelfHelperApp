package com.example.bookshelfhelper.comicbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.ComicBookRepository

class ComicBookDetailsViewModelFactory(private val repository: ComicBookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComicBookDetailsViewModel::class.java)) {
            return ComicBookDetailsViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }
}