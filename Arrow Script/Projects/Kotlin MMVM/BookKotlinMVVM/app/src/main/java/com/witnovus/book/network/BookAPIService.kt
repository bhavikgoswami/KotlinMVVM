package com.witnovus.book.network

import com.witnovus.book.model.BookInsert
import com.witnovus.book.model.BookResponse
import com.witnovus.book.utils.Constants
import retrofit2.http.GET
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * This is interface class contains all the API interface methods
 */
interface BookAPIService {
    /**
     * Get the list of Book
     * @return BookResponse class which contains list of Book
     */
    @GET(Constants.END_POINT_GET_BOOKS)
    fun getBooks(): Observable<BookResponse>

    /**
     * Sync the Book details with Server
     * @return BookResponse class which contains list of Book
     */
    @POST(Constants.END_POINT_INSERT_BOOK)
    fun insertBook(
        @Query(Constants.TITLE) title: String?,
        @Query(Constants.AUTHOR) author: String?
    ): Observable<BookInsert>
}