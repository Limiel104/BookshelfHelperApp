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

}