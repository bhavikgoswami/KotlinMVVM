package com.witnovus.book.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.witnovus.book.R
import com.witnovus.book.databinding.BookFragmentBinding
import com.witnovus.book.model.Book
import com.witnovus.book.ui.adapters.BookAdapter
import com.witnovus.book.utils.Constants
import com.witnovus.book.utils.Utils
import com.witnovus.book.viewmodel.BookViewModel
import io.paperdb.Paper

/**
 * This Book Fragment shows list of books and manages UI events.
 */
class BookFragment : Fragment(), BookAdapter.OnItemClickListener {
    private lateinit var binding: BookFragmentBinding
    private lateinit var bookAdapter: BookAdapter
    private var bookList: ArrayList<Book?>? = null
    private lateinit var viewModel: BookViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_book,
            container,
            false) // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookList = ArrayList()
        setUpToolbar()
        // Instantiate the navController using the NavHostFragment
        navController = findNavController(requireActivity(), R.id.nav_host_fragment)
        val backStackEntry = navController.getBackStackEntry(R.id.nav_graph)
        viewModel = ViewModelProvider(backStackEntry,
            HiltViewModelFactory(requireContext(), backStackEntry))[BookViewModel::class.java]

        // set RecyclerView
        setBookRecyclerView()
        viewModel.init()
        observeData()
        if (!Paper.book().contains(Constants.IS_BOOK_FETCHED_FROM_API)) {
            getBookListAPI()
        }
    }

    /**
     * This method set the toolbar
     */
    private fun setUpToolbar() {

        binding.apply {
            binding.toolBar.apply {
                titleTxtView.text = getString(R.string.app_name)
                backImgView.visibility = View.GONE
            }
        }
    }// checks internet is connected or not
    // set observer for BookList which gets from API
    // show Progress dialog
    //Call GetBook API
    /**
     * This method calls API and observers the response.
     */
    private fun getBookListAPI() {
            // checks internet is connected or not
            if (!Utils.checkInternetConnection(requireContext())) {
                Toast.makeText(requireContext(),
                    getString(R.string.msg_please_check_your_connection),
                    Toast.LENGTH_LONG).show()
            }
            // set observer for BookList which gets from API
            if (!viewModel.bookList.hasObservers()) {
                viewModel.bookList.observe(viewLifecycleOwner
                ) {
                    Utils.hideProgressDialog()
                }
            }
            // show Progress dialog

            Utils.showProgressDialog(requireContext())
            //Call GetBook API
            viewModel.getBookListAPI()
        }

    /**
     * Set Recyclerview with empty book list and managed item click listener
     */
    private fun setBookRecyclerView() {

        // click on book or read book navigate to bookDetails fragment
        bookAdapter = BookAdapter(requireContext(),
            bookList!!, this@BookFragment)
        binding.apply {
            bookRecyclerView.setHasFixedSize(true)
            bookRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            bookRecyclerView.adapter = bookAdapter
        }
    }

    /**
     * This method observes the book data from the database
     */
    private fun observeData() {
        viewModel.bookLiveList!!.observe(viewLifecycleOwner
        ) { updatedBookList: List<Book?>? ->
            if (updatedBookList != null && updatedBookList.isNotEmpty()) {
                bookList!!.clear()
                bookList!!.addAll(updatedBookList)

                bookAdapter.updateList(bookList!!)

            }
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        //navigation to detail screen with selected book id
        val bundle = Bundle()
        bundle.putInt(Constants.ID, bookList!![position]!!.id)
        navController.navigate(R.id.action_bookFragment_to_booklistFragment, bundle)

    }
}