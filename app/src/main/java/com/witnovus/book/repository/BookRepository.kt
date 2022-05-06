package com.witnovus.book.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.witnovus.book.db.AppDatabase
import com.witnovus.book.model.Book
import com.witnovus.book.model.BookResponse
import com.witnovus.book.network.BookAPIService
import com.witnovus.book.utils.Constants
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is repository class to manage the database and API's operation
 */
@Singleton
class BookRepository @Inject constructor() {
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var apiService: BookAPIService

    @Inject
    lateinit var applicationContext: Application

    // GET API to get List of Book Response
    fun getBooksResponse(): Observable<BookResponse> {
        return apiService.getBooks()
    }

    // Get list of Book from the database
    fun getBookList(): LiveData<List<Book?>?> {
        return appDatabase.bookDao()!!.getBooks()
    }


    // Insert book in book details in database
    fun insertAllBooks(bookList: List<Book?>?) {
        appDatabase.bookDao()!!.insertAllBooks(bookList)
    }

    // Get Book from the dabase for the selected book ID
    fun getSelectedBook(id: Int): Book? {
        return appDatabase.bookDao()!!.getSelectedBook(id)
    }


    // Sync Book details by calling API and update status in database.
    fun syncBook(): Boolean {
        Log.d("Sync Called", "In Progress")
        val isSyncRequired = AtomicBoolean(true)
        val syncBook = appDatabase.bookDao()!!.unSyncBook()
        if (syncBook != null) {
            apiService.insertBook(syncBook.title, syncBook.author)
                .subscribeOn(Schedulers.io())
                .map { bookInsert -> bookInsert }
                .subscribe({ result ->
                    if (result.ResponseCode == Constants.BOOK_SYNCED) {
                        appDatabase.bookDao()!!.updateBook(syncBook.id)
                    }
                    val nextSyncBook: Book? = appDatabase.bookDao()!!.unSyncBook()
                    isSyncRequired.set(nextSyncBook != null)
                }
                ) { error -> Log.e("Error", "getBook: " + error.message) }
        } else {
            isSyncRequired.set(false)
        }
        return isSyncRequired.get()
    }
}