package com.example.bookshelfhelper.comicbook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelfhelper.Event
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditComicBookViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val inputGenre = MutableLiveData<String>()
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    val inputVolumesReleased = MutableLiveData<String>()
    val inputVolumesOwned = MutableLiveData<String>()
    val inputStatus = MutableLiveData<String>()
    val inputPagesColor = MutableLiveData<String>()
    val addOrEditImageButtonText = MutableLiveData<String>()
    val addOrEditButtonText = MutableLiveData<String>()
    var inputImagePath = ""
    var isDone = MutableLiveData<Boolean>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    lateinit var comicBookToUpdate: ComicBook
    var updateMode = false

    init {
        Log.i("TAG","AddEditComicBookViewModel")
        addOrEditImageButtonText.value = "Add Comic book Cover"
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

        when{
            inputTitle.value == null -> statusMessage.value = Event("Please enter book's title")
            inputAuthor.value == null -> statusMessage.value = Event("Please enter book's author")
            inputPublisher.value == null -> statusMessage.value = Event("Please enter book's publisher")
            inputVolumesReleased.value == null -> statusMessage.value = Event("Please enter number of released volumes of comic book")
            inputVolumesOwned.value == null -> statusMessage.value = Event("Please enter number of owned volumes of comic book")
        }

        val genre : String = if(inputGenre.value == null){
            comicBookToUpdate.genre
        } else {
            inputGenre.value!!
        }

        val format : String = if(inputFormat.value == null){
            comicBookToUpdate.format
        } else {
            inputFormat.value!!
        }

        val type : String = if(inputType.value == null){
            comicBookToUpdate.type
        } else {
            inputType.value!!
        }

        val language : String = if(inputLanguage.value == null){
            comicBookToUpdate.language
        } else {
            inputLanguage.value!!
        }

        val status : String = if(inputStatus.value == null){
            comicBookToUpdate.status
        } else {
            inputStatus.value!!
        }

        val pagesColor : String = if(inputPagesColor.value == null){
            comicBookToUpdate.pagesColor
        } else {
            inputPagesColor.value!!
        }

        val imagePath : String = if(inputImagePath == ""){
            comicBookToUpdate.imagePath
        }else{
            inputImagePath
        }

        performUpdate(genre,format,type,language,status,pagesColor,imagePath)
    }

    private fun validateSave(){

        when{
            inputTitle.value == null -> statusMessage.value = Event("Please enter comic book's title")
            inputAuthor.value == null -> statusMessage.value = Event("Please enter comic book's author")
            inputPublisher.value == null -> statusMessage.value = Event("Please enter comic book's publisher")
            inputGenre.value == null -> statusMessage.value = Event("Please choose comic book's genre")
            inputFormat.value == null -> statusMessage.value = Event("Please choose comic book's format")
            inputType.value == null -> statusMessage.value = Event("Please choose comic book's type")
            inputLanguage.value == null ->  statusMessage.value = Event("Please choose comic book's language")
            inputVolumesReleased.value == null -> statusMessage.value = Event("Please enter number of released volumes of comic book")
            inputVolumesOwned.value == null -> statusMessage.value = Event("Please enter number of owned volumes of comic book")
            inputPagesColor.value == null -> statusMessage.value = Event("Please choose comic book's pages color")
            inputImagePath == "" ->  statusMessage.value = Event("Please add comic book's cover photo")
            else -> performSave()
        }
    }

    private fun performUpdate(genre: String, format: String, type: String, language: String,status: String, pagesColor: String, imagePath: String){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val volumesReleased = inputVolumesReleased.value!!.toInt()
        val volumesOwned = inputVolumesOwned.value!!.toInt()

        update(ComicBook(comicBookToUpdate.id,title,author,publisher,genre,format,type,language,volumesReleased,volumesOwned,status,pagesColor,imagePath))

        isDone.value = true
    }

    private fun performSave() {

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val genre = inputGenre.value!!
        val format = inputFormat.value!!
        val type = inputType.value!!
        val language = inputLanguage.value!!
        val volumesReleased = inputVolumesReleased.value!!.toInt()
        val volumesOwned = inputVolumesOwned.value!!.toInt()
        val status = inputStatus.value!!
        val pagesColor = inputPagesColor.value!!
        val imagePath = inputImagePath

        insert(ComicBook(0,title,author,publisher,genre,format,type,language,volumesReleased,volumesOwned,status,pagesColor,imagePath))

        isDone.value = true
    }

    fun prepareUpdateLayout(){
        inputTitle.value = comicBookToUpdate.title
        inputAuthor.value = comicBookToUpdate.author
        inputPublisher.value = comicBookToUpdate.publisher
        inputVolumesReleased.value = comicBookToUpdate.volumesReleased.toString()
        inputVolumesOwned.value = comicBookToUpdate.volumesOwned.toString()
        addOrEditImageButtonText.value = "Change Comic book Cover"
        addOrEditButtonText.value = "Update"
    }

    private fun insert(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.insert(comicBook)
        }
    }

    private fun update(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.update(comicBook)
        }
    }
}