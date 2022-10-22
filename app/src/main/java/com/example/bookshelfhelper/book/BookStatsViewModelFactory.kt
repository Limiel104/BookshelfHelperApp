package com.example.bookshelfhelper.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.BookRepository

class BookStatsViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookStatsViewModel::class.java)) {
            return BookStatsViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }
}