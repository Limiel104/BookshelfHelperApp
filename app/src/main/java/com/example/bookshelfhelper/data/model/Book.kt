package com.example.bookshelfhelper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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

) : BookshelfItem(title, author, publisher, format, type, language) {}