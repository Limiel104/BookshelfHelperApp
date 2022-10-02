package com.example.bookshelfhelper.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookshelfhelper.data.model.ComicBook

@Dao
interface ComicBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComicBook(comicBook: ComicBook)

    @Update
    suspend fun updateComicBook(comicBook: ComicBook)

    @Delete
    suspend fun deleteComicBook(comicBook: ComicBook)

    @Query("SELECT * FROM comics_table")
    fun getAllComicBooks():LiveData<List<ComicBook>>

}