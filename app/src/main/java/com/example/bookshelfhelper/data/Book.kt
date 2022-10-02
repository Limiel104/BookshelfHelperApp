package com.example.bookshelfhelper.data

import androidx.room.Entity

@Entity(tableName = "book_table")
data class Book (
    val title: String,
    val author: String,
    val publisher: String,
    val format: String,
    val type: String,
    val language: String
    ) {


}