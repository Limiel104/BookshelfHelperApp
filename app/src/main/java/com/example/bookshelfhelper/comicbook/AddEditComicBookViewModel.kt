package com.example.bookshelfhelper.comicbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditComicBookViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val addOrEditButtonText = MutableLiveData<String>()
    var isDone = MutableLiveData<Boolean>()

    lateinit var comicBookToUpdate: ComicBook
    var updateMode = false

    init {
        Log.i("TAG","AddEditComicBookViewModel")
        addOrEditButtonText.value = "Add"
    }

    fun saveOrUpdate(){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!

        if(updateMode){
            update(ComicBook(comicBookToUpdate.id,title,author,publisher,"A5","twarda okladka","Polski",-1,-1,false))
        }
        else{
            insert(ComicBook(0,title,author,publisher,"A5","twarda okladka","Polski",-1,-1,false))
        }

        isDone.value = true
    }

    fun prepareUpdateLayout(){
        inputTitle.value = comicBookToUpdate.title
        inputAuthor.value = comicBookToUpdate.author
        inputPublisher.value = comicBookToUpdate.publisher
        addOrEditButtonText.value = "Update"
    }

    fun insert(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(comicBook)
        }
    }

    fun update(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.update(comicBook)
        }
    }
}