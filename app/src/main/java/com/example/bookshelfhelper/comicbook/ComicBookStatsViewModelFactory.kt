package com.example.bookshelfhelper.comicbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.ComicBookRepository

class ComicBookStatsViewModelFactory(private val repository: ComicBookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComicBookStatsViewModel::class.java)) {
            return ComicBookStatsViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }
}