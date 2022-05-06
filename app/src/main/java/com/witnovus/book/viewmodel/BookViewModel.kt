package com.witnovus.book.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.witnovus.book.model.Book
import com.witnovus.book.repository.BookRepository
import com.witnovus.book.utils.Constants
import com.witnovus.book.utils.Utils.Companion.scheduleBookSyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.paperdb.Paper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

/**
 * This View Model class manages the API and Database operation
 */
@HiltViewModel
class BookViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var repository: BookRepository

    @Inject
    lateinit var applicationContext: Application
    var bookList = MutableLiveData<ArrayList<Book>>()
    var getSelectedBook = MutableLiveData<Book?>()

    /**
     * This method returns Book List
     *
     * @return bookLiveList
     */
    var bookLiveList: LiveData<List<Book?>?>? = null


    /**
     * This method get Book List from the database
     */
    fun init() {
        bookLiveList = repository.getBookList()
    }

    //Start worker to sync book record with server and update database
    /**
     * This method calls API to get list Of Book from the server
     */
    fun getBookListAPI() {
            repository.getBooksResponse()
                .subscribeOn(Schedulers.io())
                .map { bookResponse ->
                    val bookList: ArrayList<Book> = bookResponse.bookList
                    repository.insertAllBooks(bookList)
                    bookList
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: ArrayList<Book> ->
                    bookList.value = result
                    //Set shared preference value
                    Paper.book().write(Constants.IS_BOOK_FETCHED_FROM_API, true)

                    //Start worker to sync book record with server and update database
                    scheduleBookSyncWorker(applicationContext)
                }
                ) { error: Throwable ->
                    Log.e(TAG,
                        "getBook API Error: " + error.message)
                }
        }

    /**
     * This method gets Book detail for the selected Book Id
     *
     * @param id : Book Id
     */
    fun getSelectedBook(id: Int) {
        getSelectedBook.value = repository.getSelectedBook(id)
    }

    companion object {
        private const val TAG = "BookViewModel"
    }
}