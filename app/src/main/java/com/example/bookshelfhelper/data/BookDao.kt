package com.example.bookshelfhelper.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookshelfhelper.data.model.Book

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books_table")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books_table WHERE title LIKE :searchQuery OR author LIKE :searchQuery OR publisher LIKE :searchQuery")
    fun searchBooks(searchQuery: String): LiveData<List<Book>>
}