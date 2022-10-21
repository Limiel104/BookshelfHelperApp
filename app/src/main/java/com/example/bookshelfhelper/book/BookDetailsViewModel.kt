package com.example.bookshelfhelper.book

import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository

class BookDetailsViewModel(private val repository: BookRepository) : ViewModel() {

    lateinit var bookToDisplay: Book
}