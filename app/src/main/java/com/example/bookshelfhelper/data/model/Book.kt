package com.example.bookshelfhelper.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "books_table")
@Parcelize
open class Book (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val genre: String,
    val format: String,
    val type: String,
    val language: String,
    val imagePath: String

) : Parcelable {}