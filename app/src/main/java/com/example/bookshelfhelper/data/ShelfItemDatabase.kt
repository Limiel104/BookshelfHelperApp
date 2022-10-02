package com.example.bookshelfhelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.model.ComicBook

@Database(entities = [Book::class, ComicBook::class], version = 1)
abstract class ShelfItemDatabase : RoomDatabase() {

    abstract val bookDao: BookDao
    abstract val comicBookDao: ComicBookDao



    companion object{
        @Volatile
        private var INSTANCE: ShelfItemDatabase? = null
        fun getInstance(context: Context): ShelfItemDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ShelfItemDatabase::class.java,
                        "shelf_item_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}