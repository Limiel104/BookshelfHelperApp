package com.example.bookshelfhelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditItemViewModel(private  val repository: BookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()

    //var flaga: Boolean = false

    //mutable button add/update
    //fun saveOrUpdate

    fun saveOrUpdate(){
        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!

        insert(Book(0,title,author,publisher,"A5","twarda okladka","Polski"))
        //flaga = true
    }

    fun insert(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(book)
        }
    }

    //update fun



}