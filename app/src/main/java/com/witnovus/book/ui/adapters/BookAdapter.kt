package com.witnovus.book.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import java.util.ArrayList
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.witnovus.book.R
import com.witnovus.book.databinding.BookAdapterBinding
import com.witnovus.book.model.Book
import com.witnovus.book.utils.Constants

/**
 * This Adapter class is used to show list of Book with details
 */
class BookAdapter(
    var context: Context,
    private var bookList: ArrayList<Book?>,
    private var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    private lateinit  var binding: BookAdapterBinding
    private lateinit var authorName: String
    private lateinit var bookPublishInformation: String

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.row_book, parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {

            if (bookList[position] != null) {
                val book = bookList[position]
                if (book!!.author.isNotEmpty()) {
                    authorName = context.getString(R.string.lbl_auther) + Constants.SPACE + book.author
                   bookAutherTxtView.text = authorName
                }
                if (book.title.isNotEmpty()) {
                  bookTitleTxtView.text = book.title
                }
                if (book.description.isNotEmpty()) {
                   bookDescriptionTxtView.text = book.description
                }
                if (book.published != 0) {
                    bookPublishInformation =
                        context.getString(R.string.lbl_published) + Constants.SPACE + book.published
                  bookPublishedTxtView.text = bookPublishInformation
                }
                if (book.uploaded == Constants.BOOK_SYNCED) {
                    syncImgView.background = ContextCompat.getDrawable(context,
                        R.drawable.ic_sync_done)
                } else {
                    syncImgView.background =ContextCompat.getDrawable(context,
                        R.drawable.ic_sync_pending)
                }
              bookCardView.setOnClickListener { view: View? ->
                    onItemClickListener.onItemClick(view,
                        position)
                }
              readTxtView.setOnClickListener { view: View? ->
                    onItemClickListener.onItemClick(view,
                        position)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class ViewHolder(val binding: BookAdapterBinding) : RecyclerView.ViewHolder(
        binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(updatedList: ArrayList<Book?>) {
        bookList = updatedList
        notifyDataSetChanged()
    }
}