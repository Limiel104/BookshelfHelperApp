package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookListViewModel(private val repository: BookRepository) : ViewModel() {

    val books = repository.books //ten obiekt jest obserwowany w listFragmwnt, lista wszytskich ksiazek

    val addOrEditButtonText = MutableLiveData<String>()

    lateinit var bookToUpdate : Book
    var updateRequested = false

    init {
        Log.i("TAG","BookListViewModel")
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
            returnToInitLayout()
        }
    }

    private fun returnToInitLayout(){
        addOrEditButtonText.value = "Add"
        updateRequested = false
        bookToUpdate = Book(-1,"","","","","","","")
    }

    fun delete(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.delete(book)
        }
    }

    fun searchBooks(searchQuery: String): LiveData<List<Book>>{
        return repository.searchBooks(searchQuery)
    }

    fun getAllBooks(): LiveData<List<Book>>{
        return repository.getAllBooks()
    }

    fun getFilteredType(searchQuery: String): LiveData<List<Book>>{
        return  repository.getFilteredType(searchQuery)
    }
}