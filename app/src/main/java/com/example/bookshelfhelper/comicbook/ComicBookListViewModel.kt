package com.example.bookshelfhelper.comicbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComicBookListViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val comicBooks = repository.comicBooks

    val addOrEditButtonText = MutableLiveData<String>()

    lateinit var comicBookToUpdate : ComicBook
    var updateRequested = false

    init {
        Log.i("TAG","ComicBookListViewModel")
        addOrEditButtonText.value = "Add"
    }

    fun initUpdate(comicBook: ComicBook){
        Log.i("TAG","isUpdate")
        if (!updateRequested){
            Log.i("TAG","klikam")
            addOrEditButtonText.value = "Update"
            updateRequested = true
            comicBookToUpdate = comicBook
        }
        else{
            Log.i("TAG","odklikuje")
            returnToInitLayout()
        }
    }

    private fun returnToInitLayout(){
        addOrEditButtonText.value = "Add"
        updateRequested = false
        comicBookToUpdate = ComicBook(-1,"","","","","","",-1,-1,"","")
    }

    fun delete(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.delete(comicBook)
        }
    }

    fun searchComicBooks(searchQuery: String): LiveData<List<ComicBook>> {
        return repository.searchComicBooks(searchQuery)
    }
}