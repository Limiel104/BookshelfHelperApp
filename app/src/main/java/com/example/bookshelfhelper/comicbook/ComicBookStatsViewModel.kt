package com.example.bookshelfhelper.comicbook

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.repository.ComicBookRepository

class ComicBookStatsViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val comicBooks = repository.comicBooks

    init {
        Log.i("TAG","ComicBookStatsViewModel")
    }
}