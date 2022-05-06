package com.witnovus.book.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.witnovus.book.model.Book

/**
 * Database management class
 */
@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao?
}