package com.example.bookshelfhelper

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository

class BookListViewModel(private val repository: BookRepository) : ViewModel() {

    val books = repository.books //ten obiekt jest obserwowany w listFragmwnt

    val addOrEditButtonText = MutableLiveData<String>()

    lateinit var bookToUpdate : Book
    var updateRequested = false

    init {
        Log.i("TAG","ListViewModel")
        addOrEditButtonText.value = "Add"
    }

    fun initUpdate(book: Book){
        Log.i("TAG","isUpdate")
        if (!updateRequested){
            Log.i("TAG","klikam")
            addOrEditButtonText.value = "Update"
            updateRequested = true
            bookToUpdate = book
        }
        else{
            Log.i("TAG","odklikuje")
            addOrEditButtonText.value = "Add"
            updateRequested = false
            bookToUpdate = Book(-1,"","","","","","")
        }
    }
}