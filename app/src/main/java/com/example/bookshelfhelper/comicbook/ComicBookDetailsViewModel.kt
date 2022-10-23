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

class ComicBookDetailsViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val inputGenre = MutableLiveData<String>()
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    private val inputVolumesReleased = MutableLiveData<Int>()
    private val inputVolumesOwned = MutableLiveData<Int>()
    val inputVolumes = MutableLiveData<String>()
    val inputStatus = MutableLiveData<String>()
    val inputPagesColor = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    lateinit var comicBookToDisplay : ComicBook

    init {
        Log.i("TAG","ComicBookDetailsViewModel")
    }

    fun setLayout(){
        inputTitle.value = comicBookToDisplay.title
        inputAuthor.value = comicBookToDisplay.author
        inputPublisher.value = comicBookToDisplay.publisher
        inputGenre.value = comicBookToDisplay.genre
        inputFormat.value = comicBookToDisplay.format
        inputType.value = comicBookToDisplay.type
        inputLanguage.value = getLanguageValue()
        inputVolumesReleased.value = comicBookToDisplay.volumesReleased
        inputVolumesOwned.value = comicBookToDisplay.volumesOwned
        inputVolumes.value = getVolumesValue(comicBookToDisplay.volumesReleased,comicBookToDisplay.volumesOwned)
        inputStatus.value = comicBookToDisplay.status
        inputPagesColor.value = comicBookToDisplay.pagesColor
    }

    private fun getLanguageValue(): String {
        return when (comicBookToDisplay.language) {
            "Polish" -> return "PL"
            "English" -> return "ENG"
            "Japanese" -> return "JPN"
            else -> ""
        }
    }

    private fun getVolumesValue(volumesReleased: Int, volumesOwned: Int): String {
        return "$volumesOwned/$volumesReleased"
    }

    fun setStatus(){
        when (inputStatus.value) {
            "Incompleted" -> {
                inputStatus.value = "Dropped"
                inputStatus.value?.let { inputVolumesReleased.value?.let { it1 ->
                    inputVolumesOwned.value?.let { it2 ->
                        performUpdate(it,
                            it1, it2
                        )
                    }
                } }
            }
            "Dropped" -> {
                inputStatus.value = "Incompleted"
                inputStatus.value?.let { inputVolumesReleased.value?.let { it1 ->
                    inputVolumesOwned.value?.let { it2 ->
                        performUpdate(it,
                            it1, it2
                        )
                    }
                } }
            }
            else -> statusMessage.value = Event("Can't change comic book status when Completed")
        }
    }

    fun setOwnedVolumes(){
        if (inputVolumesOwned.value!! + 1 > inputVolumesReleased.value!!) {
            statusMessage.value = Event("Can't change comic book owned volumes to more than released")
        }
        else if (inputVolumesReleased.value == inputVolumesOwned.value!! + 1){
            inputVolumesOwned.value = inputVolumesOwned.value!! + 1
            inputVolumes.value = getVolumesValue(inputVolumesReleased.value!!,inputVolumesOwned.value!!)
            inputStatus.value = "Completed"
            performUpdate(inputStatus.value!!,inputVolumesReleased.value!!,inputVolumesOwned.value!!)
            statusMessage.value = Event("Status set as Completed")
        }
        else {
            inputVolumesOwned.value = inputVolumesOwned.value!! + 1
            inputVolumes.value = getVolumesValue(inputVolumesReleased.value!!,inputVolumesOwned.value!!)
            performUpdate(inputStatus.value!!,inputVolumesReleased.value!!,inputVolumesOwned.value!!)
        }
    }

    fun setReleasedVolumes(){
        if (inputStatus.value == "Completed") {
            inputStatus.value = "Incompleted"
        }
        inputVolumesReleased.value = inputVolumesReleased.value!! + 1
        inputVolumes.value = getVolumesValue(inputVolumesReleased.value!!,inputVolumesOwned.value!!)
        performUpdate(inputStatus.value!!,inputVolumesReleased.value!!,inputVolumesOwned.value!!)
    }

    private fun performUpdate(status: String, volumesReleased: Int, volumesOwned: Int,){
        val title = inputTitle.value!!
        val author = inputAuthor.value!!
        val publisher = inputPublisher.value!!
        val genre = inputGenre.value!!
        val format = inputFormat.value!!
        val type = inputType.value!!
        val language = inputLanguage.value!!
        val pagesColor = inputPagesColor.value!!
        val imagePath = comicBookToDisplay.imagePath

        update(ComicBook(comicBookToDisplay.id,title,author,publisher,genre,format,type,language,volumesReleased,volumesOwned,status,pagesColor,imagePath))
    }

    private fun update(comicBook: ComicBook){
        viewModelScope.launch( Dispatchers.IO) {
            repository.update(comicBook)
        }
    }
}