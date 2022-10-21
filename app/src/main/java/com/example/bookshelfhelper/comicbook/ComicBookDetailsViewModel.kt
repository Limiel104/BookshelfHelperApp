package com.example.bookshelfhelper.comicbook

import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository

class ComicBookDetailsViewModel(private val repository: ComicBookRepository) : ViewModel() {

    lateinit var comicBookToDisplay : ComicBook
}