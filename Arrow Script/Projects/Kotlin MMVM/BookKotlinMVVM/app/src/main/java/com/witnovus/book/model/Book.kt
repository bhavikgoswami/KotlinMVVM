package com.witnovus.book.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * This model class used for both API response as well database operation.
 */
@Entity(tableName = "book_table")
class Book(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @SerializedName(
        "author") var author: String,
    @SerializedName("description") var description: String,
    @SerializedName(
        "published") var published: Int,
    @SerializedName("title") var title: String,
    var uploaded: Int = 0,
)
