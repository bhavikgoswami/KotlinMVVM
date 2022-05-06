package com.witnovus.book.model

import java.util.ArrayList
import com.google.gson.annotations.SerializedName

/**
 * This is Book List API Response model class
 */
data class BookResponse(
    @SerializedName("Books") var bookList: ArrayList<Book>,
    @SerializedName("Last_Update") var lastUpdate: String,
    @SerializedName("Num_Books") var bookNumber: Int
)