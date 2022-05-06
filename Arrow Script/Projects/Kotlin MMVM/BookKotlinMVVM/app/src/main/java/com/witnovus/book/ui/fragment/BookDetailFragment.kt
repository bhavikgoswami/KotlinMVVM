package com.witnovus.book.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.witnovus.book.R
import com.witnovus.book.databinding.BookDetailFragmentBinding
import com.witnovus.book.model.Book
import com.witnovus.book.utils.Constants
import com.witnovus.book.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment shows Book Details and manages all the UI events.
 */
@AndroidEntryPoint
class BookDetailFragment : Fragment() {
    private lateinit var binding: BookDetailFragmentBinding
    private lateinit var viewModel: BookViewModel
    private lateinit var selectedBook: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_detail, container, false)

        //set up navController
        val navController = findNavController(requireActivity(), R.id.nav_host_fragment)
        val backStackEntry = navController.getBackStackEntry(R.id.nav_graph)
        viewModel = ViewModelProvider(backStackEntry,
            HiltViewModelFactory(requireContext(), backStackEntry))[BookViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get Bundle from arguments
        val bundle = arguments
        if (bundle != null) {
            val bookId = bundle.getInt(Constants.ID)
            getBook(bookId)
        }
    }

    /**
     * This method request to get the Book details for the selected Book ID
     * @param id : Book ID
     */
    private fun getBook(id: Int) {
        // set observer for selected Book which we are get from viewmodel
        if (!viewModel.getSelectedBook.hasObservers()) {
            viewModel.getSelectedBook.observe(viewLifecycleOwner) { book: Book? ->
                selectedBook = book!!
                //set data to view
                setBook(selectedBook)
                setUpToolbar(selectedBook.title)
            }
        }
        viewModel.getSelectedBook(id)
    }

    /**
     * This method sets the Book details in Views
     * @param selectedBook : Book
     */
    private fun setBook(selectedBook: Book?) {
        binding.apply {
            bookAutherTxtView.append(Constants.SPACE + selectedBook!!.author)
            bookTitleTxtView.text = selectedBook.title
            bookDescriptionTxtView.append(selectedBook.description)
            bookPublishedTxtView.append(Constants.SPACE + selectedBook.published)

        }
    }

    /**
     * Set the toolbar
     * @param title : String
     */
    private fun setUpToolbar(title: String) {
        binding.toolBar.apply {
            titleTxtView.text = title
            backImgView.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}