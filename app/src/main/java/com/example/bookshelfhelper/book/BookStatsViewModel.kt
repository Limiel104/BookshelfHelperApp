package com.example.bookshelfhelper.book

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.repository.BookRepository

class BookStatsViewModel(private val repository: BookRepository) : ViewModel() {

    val books = repository.books

    init {
        Log.i("TAG","BookStatsViewModel")
    }
}