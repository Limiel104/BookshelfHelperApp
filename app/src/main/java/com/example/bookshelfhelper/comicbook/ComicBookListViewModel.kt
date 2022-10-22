package com.example.bookshelfhelper.comicbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComicBookListViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val comicBooks = repository.comicBooks

    lateinit var comicBookToUpdate : ComicBook
    var updateRequested = false

    init {
        Log.i("TAG","ComicBookListViewModel")
    }

    fun initUpdate(comicBook: ComicBook){
        if (!updateRequested){
            updateRequested = true
            comicBookToUpdate = comicBook
        }
        else{
            returnToInitLayout()
        }
    }

    private fun returnToInitLayout(){
        updateRequested = false
        comicBookToUpdate = ComicBook(-1,"","","","","","","",-1,-1,"","","")
    }

    fun delete(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.delete(comicBook)
        }
    }

    fun searchComicBooks(searchQuery: String): LiveData<List<ComicBook>> {
        return repository.searchComicBooks(searchQuery)
    }

    fun getAllComicBooks(): LiveData<List<ComicBook>>{
        return repository.getAllComicBooks()
    }

    fun getFilteredGenre(searchQuery: String): LiveData<List<ComicBook>>{
        return repository.getFilteredGenre(searchQuery)
    }
}