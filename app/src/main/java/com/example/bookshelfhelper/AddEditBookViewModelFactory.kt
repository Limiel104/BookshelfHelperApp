package com.example.bookshelfhelper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.BookRepository

class AddEditBookViewModelFactory (private val repository: BookRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditBookViewModel::class.java)) {
            return AddEditBookViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }

}