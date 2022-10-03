package com.example.bookshelfhelper

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditBookViewModel(private  val repository: BookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()

    var isDone = MutableLiveData<Boolean>()

    init {
        //isDone.value = false
        Log.i("TAG","AddEditBookViewModel")
    }

    //mutable button add/update

    fun saveOrUpdate(){
        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!

        insert(Book(0,title,author,publisher,"A5","twarda okladka","Polski"))

        isDone.value = true
    }

    fun insert(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(book)
        }
    }

    //update fun



}