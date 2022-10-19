package com.example.bookshelfhelper.data.repository

import androidx.lifecycle.LiveData
import com.example.bookshelfhelper.data.ComicBookDao
import com.example.bookshelfhelper.data.model.ComicBook

class ComicBookRepository(private val comicBookDao: ComicBookDao) {

    val comicBooks = comicBookDao.getAllComicBooks()

    suspend fun insert(comicBook: ComicBook){
        comicBookDao.insertComicBook(comicBook)
    }

    suspend fun update(comicBook: ComicBook){
        comicBookDao.updateComicBook(comicBook)
    }

    suspend fun delete(comicBook: ComicBook){
        comicBookDao.deleteComicBook(comicBook)
    }

    fun getAllComicBooks(): LiveData<List<ComicBook>>{
        return comicBookDao.getAllComicBooks()
    }

    fun searchComicBooks(searchQuery: String): LiveData<List<ComicBook>>{
        return comicBookDao.searchComicBooks(searchQuery)
    }

    fun getFilteredGenre(searchQuery: String): LiveData<List<ComicBook>>{
        return comicBookDao.getFilteredGenre(searchQuery)
    }
}