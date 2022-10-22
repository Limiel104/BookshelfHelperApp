package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookListViewModel(private val repository: BookRepository) : ViewModel() {

    val books = repository.books //ten obiekt jest obserwowany w listFragmwnt, lista wszytskich ksiazek

    lateinit var bookToUpdate : Book
    var updateRequested = false

    init {
        Log.i("TAG","BookListViewModel")
    }

    fun initUpdate(book: Book){
        if (!updateRequested){
            updateRequested = true
            bookToUpdate = book
        }
        else{
            returnToInitLayout()
        }
    }

    private fun returnToInitLayout(){
        updateRequested = false
        bookToUpdate = Book(-1,"","","","","","","",-1,"")
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

    fun getFilteredGenre(searchQuery: String): LiveData<List<Book>>{
        return  repository.getFilteredGenre(searchQuery)
    }

    fun getFilteredType(searchQuery: String): LiveData<List<Book>>{
        return  repository.getFilteredType(searchQuery)
    }
}