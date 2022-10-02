package com.example.bookshelfhelper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookshelfhelper.data.repository.BookRepository

class AddEditViewModelFactory (private val repository: BookRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditItemViewModel::class.java)) {
            return AddEditItemViewModel(repository) as T
        }
        throw  IllegalAccessException("Unknown ViewModel Class")
    }

}