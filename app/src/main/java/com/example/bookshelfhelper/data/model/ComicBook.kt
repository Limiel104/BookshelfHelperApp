package com.example.bookshelfhelper.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "comics_table")
@Parcelize
class ComicBook(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val format: String,
    val type: String,
    val language: String,
    val volumesReleased: Int,
    val volumesOwned: Int,
    val pagesColor: String,
    val imagePath: String

) : Parcelable {}