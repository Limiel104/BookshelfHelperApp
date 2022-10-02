package com.example.bookshelfhelper.data.repository

import com.example.bookshelfhelper.data.BookDao
import com.example.bookshelfhelper.data.model.Book

class BookRepository(private val bookDao: BookDao) {

    val books = bookDao.getAllBooks()

    suspend fun insert(book: Book){
        bookDao.insertBook(book)
    }

    suspend fun update(book: Book){
        bookDao.updateBook(book)
    }

    suspend fun delete(book: Book){
        bookDao.deleteBook(book)
    }
}