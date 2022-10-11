package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditBookViewModel(private val repository: BookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    val addOrEditButtonText = MutableLiveData<String>()
    var isDone = MutableLiveData<Boolean>()

    lateinit var bookToUpdate: Book
    var updateMode = false

    init {
        Log.i("TAG","AddEditBookViewModel")
        addOrEditButtonText.value = "Add"
    }

    fun saveOrUpdate(){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val format = inputFormat.value!!
        val type = inputType.value!!
        val language = inputLanguage.value!!

        if(updateMode){
            update(Book(bookToUpdate.id,title,author,publisher,format,type,language))
        }
        else{
            insert(Book(0,title,author,publisher,format,type,language))
        }

        isDone.value = true
    }

    fun prepareUpdateLayout(){
        inputTitle.value = bookToUpdate.title
        inputAuthor.value = bookToUpdate.author
        inputPublisher.value = bookToUpdate.publisher
        inputFormat.value = bookToUpdate.format
        inputType.value = bookToUpdate.type
        inputLanguage.value = bookToUpdate.language
        addOrEditButtonText.value = "Update"
    }

    fun insert(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(book)
        }
    }

    fun update(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.update(book)
        }
    }
}