package com.example.bookshelfhelper

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository

class ListViewModel(private val repository: BookRepository) : ViewModel() {

    val books = repository.books //ten obiekt jest obserwowany w listFragmwnt

    val addOrEditButtonText = MutableLiveData<String>()

    init {
        addOrEditButtonText.value = "Dodaj"
        Log.i("TAG","ListViewModel")
    }

//    fun insert(book: Book){
//
//    }



}