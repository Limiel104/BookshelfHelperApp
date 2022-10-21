package com.example.bookshelfhelper.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.BookRepository

class BookDetailsViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailsViewModel::class.java)) {
            return BookDetailsViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }
}