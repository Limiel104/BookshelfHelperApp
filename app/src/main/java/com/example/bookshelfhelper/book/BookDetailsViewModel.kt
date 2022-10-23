package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository

class BookDetailsViewModel(private val repository: BookRepository) : ViewModel() {

    val inputTitle = MutableLiveData<String>()
    val inputAuthor = MutableLiveData<String>()
    val inputPublisher = MutableLiveData<String>()
    val inputGenre = MutableLiveData<String>()
    val inputFormat = MutableLiveData<String>()
    val inputType = MutableLiveData<String>()
    val inputLanguage = MutableLiveData<String>()
    val inputYearBought = MutableLiveData<String>()

    lateinit var bookToDisplay: Book

    init {
        Log.i("TAG","BookDetailsViewModel")
    }

    fun setLayout(){
        inputTitle.value = bookToDisplay.title
        inputAuthor.value = bookToDisplay.author
        inputPublisher.value = bookToDisplay.publisher
        inputGenre.value = bookToDisplay.genre
        inputFormat.value = bookToDisplay.format
        inputType.value = bookToDisplay.type
        inputLanguage.value = getLanguageValue()
        inputYearBought.value = bookToDisplay.yearBought.toString()
    }

    private fun getLanguageValue(): String {
        return when (bookToDisplay.language) {
            "Polish" -> return "PL"
            "English" -> return "ENG"
            "Japanese" -> return "JPN"
            else -> ""
        }
    }
}