package com.example.bookshelfhelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.model.ComicBook

@Database(entities = [Book::class, ComicBook::class], version = 1)
abstract class BookshelfDatabase : RoomDatabase() {

    abstract val bookDao: BookDao
    abstract val comicBookDao: ComicBookDao



    companion object{
        @Volatile
        private var INSTANCE: BookshelfDatabase? = null
        fun getInstance(context: Context): BookshelfDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookshelfDatabase::class.java,
                        "bookshelf_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}