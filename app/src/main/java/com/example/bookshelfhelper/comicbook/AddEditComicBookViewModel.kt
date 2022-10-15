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
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    val inputVolumesReleased = MutableLiveData<String>()
    val inputVolumesOwned = MutableLiveData<String>()
    val inputPagesColor = MutableLiveData<String>()
    val addOrEditButtonText = MutableLiveData<String>()
    var isDone = MutableLiveData<Boolean>()

    lateinit var comicBookToUpdate: ComicBook
    var updateMode = false

    init {
        Log.i("TAG","AddEditComicBookViewModel")
        addOrEditButtonText.value = "Add"
    }

    fun saveOrUpdate(){

        if(updateMode){

            val format : String = if(inputFormat.value == null){
                Log.i("TAG","format is null")
                comicBookToUpdate.format
            } else {
                inputFormat.value!!
            }

            val type : String = if(inputType.value == null){
                Log.i("TAG","type is null")
                comicBookToUpdate.type
            } else {
                inputType.value!!
            }

            val language : String = if(inputLanguage.value == null){
                Log.i("TAG","language is null")
                comicBookToUpdate.language
            } else {
                inputLanguage.value!!
            }

            val pagesColor : String = if(inputPagesColor.value == null){
                Log.i("TAG","pagesColor is null")
                comicBookToUpdate.pagesColor
            } else {
                inputPagesColor.value!!
            }

            performUpdate(format,type,language,pagesColor)
        }
        else{
            performSave()
        }
    }

    private fun performUpdate(format: String, type: String, language: String, pagesColor: String){

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val volumesReleased = inputVolumesReleased.value!!.toInt()
        val volumesOwned = inputVolumesOwned.value!!.toInt()

        update(ComicBook(comicBookToUpdate.id,title,author,publisher,format,type,language,volumesReleased,volumesOwned,pagesColor))

        isDone.value = true
    }

    private fun performSave() {

        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val format = inputFormat.value!!
        val type = inputType.value!!
        val language = inputLanguage.value!!
        val volumesReleased = inputVolumesReleased.value!!.toInt()
        val volumesOwned = inputVolumesOwned.value!!.toInt()
        val pagesColor = inputPagesColor.value!!

        insert(ComicBook(0,title,author,publisher,format,type,language,volumesReleased,volumesOwned,pagesColor))

        isDone.value = true
    }

    fun prepareUpdateLayout(){
        inputTitle.value = comicBookToUpdate.title
        inputAuthor.value = comicBookToUpdate.author
        inputPublisher.value = comicBookToUpdate.publisher
        inputVolumesReleased.value = comicBookToUpdate.volumesReleased.toString()
        inputVolumesOwned.value = comicBookToUpdate.volumesOwned.toString()
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