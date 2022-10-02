package com.example.bookshelfhelper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.example.bookshelfhelper.data.model.ShelfItem

@Entity(tableName = "books_table")
open class Book (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    title: String,
    author: String,
    publisher: String,
    format: String,
    type: String,
    language: String

) : ShelfItem(title, author, publisher, format, type, language) {}