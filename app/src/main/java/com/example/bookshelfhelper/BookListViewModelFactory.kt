package com.example.bookshelfhelper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.BookRepository

class BookListViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookListViewModel::class.java)) {
            return BookListViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }
}