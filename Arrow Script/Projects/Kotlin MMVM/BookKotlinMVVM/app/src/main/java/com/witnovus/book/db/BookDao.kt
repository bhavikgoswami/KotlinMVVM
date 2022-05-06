package com.witnovus.book.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.witnovus.book.model.Book


/**
 * This interface contains all the database related operation methods.
 */
@Dao
interface BookDao {
    // Get list of Book from the database
    /**
     * get bookList from book_table
     */
    @Query("SELECT * FROM book_table")
    fun getBooks(): LiveData<List<Book?>?>

    /**
     * get selected book from book_table
     */
    @Query("SELECT * FROM book_table WHERE id = :id")
    fun getSelectedBook(id: Int): Book?

    @Query("SELECT * FROM book_table WHERE uploaded=0 ORDER BY ROWID ASC LIMIT 1")
    fun unSyncBook(): Book?

    /**
     * insert selected book in book_table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(book: List<Book?>?)

    /**
     * update selected book in book_table and change status of uploaded book
     */
    @Query("UPDATE book_table SET uploaded = 1 WHERE id = :id")
    fun updateBook(id: Int)
}