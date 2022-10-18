package com.example.bookshelfhelper.data.repository

import androidx.lifecycle.LiveData
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

    fun getAllBooks(): LiveData<List<Book>>{
        return bookDao.getAllBooks()
    }

    fun searchBooks(searchQuery: String): LiveData<List<Book>>{
        return bookDao.searchBooks(searchQuery)
    }

    fun getFilteredType(searchQuery: String): LiveData<List<Book>>{
        return bookDao.getFilteredType(searchQuery)
    }
}