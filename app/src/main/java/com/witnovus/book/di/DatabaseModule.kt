package com.witnovus.book.di

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.witnovus.book.db.AppDatabase
import com.witnovus.book.db.BookDao
import com.witnovus.book.model.Book
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton

/**
 * Provider of singleton objects of Database classes
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    /**
     * Provider of Book
     * @return the selected Book data
     */
    @Provides
    @Singleton
    fun getSelectedBook(id: Int, bookDao: BookDao): Book? {
        return bookDao.getSelectedBook(id)
    }

    companion object {
        /**
         * Provider of Room Database
         * @return the AppDatabase class
         */

        @Provides
        @Singleton
        fun provideBookDB(application: Application?): AppDatabase {
            return Room.databaseBuilder(application!!, AppDatabase::class.java, "books")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }

        /**
         * Provider of BookList
         * @return the BookList
         */
        @Provides
        @Singleton
        fun provideBookDao(bookDao: BookDao): LiveData<List<Book?>?> {
            return bookDao.getBooks()
        }
    }
}