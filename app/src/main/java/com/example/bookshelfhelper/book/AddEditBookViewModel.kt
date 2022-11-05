package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.Event
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditBookViewModel(private val repository: BookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val inputGenre = MutableLiveData<String>()
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    val inputYearBought = MutableLiveData<String>()
    val addOrEditImageButtonText = MutableLiveData<String>()
    val addOrEditButtonText = MutableLiveData<String>()
    val fragmentTitle = MutableLiveData<String>()
    var inputImagePath = ""
    var isDone = MutableLiveData<Boolean>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    lateinit var bookToUpdate: Book
    var updateMode = false

    init {
        Log.i("TAG","AddEditBookViewModel")
        fragmentTitle.value = "Add new Book"
        addOrEditImageButtonText.value = "Add Book Cover"
        addOrEditButtonText.value = "Add"
    }

    fun saveOrUpdate(){

        if(updateMode){
            validateUpdate()
        }
        else{
            validateSave()
        }
    }

    private fun validateUpdate(){

        val genre : String = if(inputGenre.value == null){
            bookToUpdate.genre
        } else {
            inputGenre.value!!
        }

        val format : String = if(inputFormat.value == null){
            bookToUpdate.format
        } else {
            inputFormat.value!!
        }

        val type : String = if(inputType.value == null){
            bookToUpdate.type
        } else {
            inputType.value!!
        }

        val language : String = if(inputLanguage.value == null){
            bookToUpdate.language
        } else {
            inputLanguage.value!!
        }

        val imagePath : String = if(inputImagePath == ""){
            bookToUpdate.imagePath
        }else{
            inputImagePath
        }

        when{
            inputTitle.value == "" -> statusMessage.value = Event("Please enter book's title")
            inputAuthor.value == "" -> statusMessage.value = Event("Please enter book's author")
            inputPublisher.value == "" -> statusMessage.value = Event("Please enter book's publisher")
            inputYearBought.value == "" -> statusMessage.value = Event("Please enter year the book was bought")
            else -> performUpdate(genre,format,type,language,imagePath)
        }
    }

    private fun validateSave(){

        when{
            inputTitle.value == null -> statusMessage.value = Event("Please enter book's title")
            inputAuthor.value == null -> statusMessage.value = Event("Please enter book's author")
            inputPublisher.value == null -> statusMessage.value = Event("Please enter book's publisher")
            inputGenre.value == null -> statusMessage.value = Event("Please choose book's genre")
            inputFormat.value == null -> statusMessage.value = Event("Please choose book's format")
            inputType.value == null -> statusMessage.value = Event("Please choose book's type")
            inputLanguage.value == null ->  statusMessage.value = Event("Please choose book's language")
            inputYearBought.value == null -> statusMessage.value = Event("Please enter year the book was bought")
            inputImagePath == "" ->  statusMessage.value = Event("Please add book's cover photo")
            else -> performSave()
        }
    }

    private fun performUpdate(genre: String, format: String, type: String, language: String, imagePath: String){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val yearBought = inputYearBought.value!!.toInt()

        update(Book(bookToUpdate.id,title,author,publisher,genre,format,type,language,yearBought,imagePath))

        isDone.value = true
    }

    private fun performSave(){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val genre = inputGenre.value!!
        val format = inputFormat.value!!
        val type = inputType.value!!
        val language = inputLanguage.value!!
        val yearBought = inputYearBought.value!!.toInt()
        val imagePath = inputImagePath

        insert(Book(0,title,author,publisher,genre,format,type,language,yearBought,imagePath))

        isDone.value = true
    }

    fun prepareUpdateLayout(){
        fragmentTitle.value = "Edit Book"
        inputTitle.value = bookToUpdate.title
        inputAuthor.value = bookToUpdate.author
        inputPublisher.value = bookToUpdate.publisher
        inputYearBought.value = bookToUpdate.yearBought.toString()
        addOrEditImageButtonText.value = "Change Book Cover"
        addOrEditButtonText.value = "Update"
    }

    private fun insert(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(book)
            withContext(Dispatchers.Main){
                statusMessage.value = Event("New Book Inserted")
            }
        }
    }

    private fun update(book: Book){
        viewModelScope.launch( Dispatchers.IO) {
            repository.update(book)
            withContext(Dispatchers.Main){
                statusMessage.value = Event("Book Updated")
            }
        }
    }
}