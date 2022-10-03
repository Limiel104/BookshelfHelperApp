package com.example.bookshelfhelper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics_table")
class ComicBook(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val volumesReleased: Int,
    val volumesOwned: Int,
    val isBlackAndWhite: Boolean,
    title: String,
    author: String,
    publisher: String,
    format: String,
    type: String,
    language: String,

) : BookshelfItem(title, author, publisher, format, type, language) {}