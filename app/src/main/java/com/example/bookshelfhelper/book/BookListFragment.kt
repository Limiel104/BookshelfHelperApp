package com.example.bookshelfhelper.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.SwipeToDelete
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentBookListBinding
import com.google.android.material.chip.Chip

class BookListFragment : Fragment(){

    private lateinit var bookViewModel: BookListViewModel
    private lateinit var binding: FragmentBookListBinding
    private lateinit var adapter: BookListRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container,false)

        initRecycleView()

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookListViewModelFactory(repository)
        bookViewModel = ViewModelProvider(this, factory)[BookListViewModel::class.java]
        binding.bookListViewModel = bookViewModel
        binding.lifecycleOwner = this

        setOnClickListeners()
        displayBooksList()
        setSwipeToDelete()
        setSearchView()
        setFilterChips()

        return  binding.root
    }

    private fun initRecycleView(){
        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookListRecycleViewAdapter() { selectedItem: Book ->
            listItemClicked(
                selectedItem
            )}
        binding.recycleView.adapter = adapter
    }

    private fun displayBooksList(){
        bookViewModel.books.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun listItemClicked(book: Book){
        bookViewModel.initUpdate(book)

        if(bookViewModel.updateRequested){
            binding.addOrEditButton.setImageDrawable(resources.getDrawable(R.drawable.ic_edit))
            binding.detailsButton.visibility = View.VISIBLE
        }
        else{
            binding.addOrEditButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
            binding.detailsButton.visibility = View.GONE
        }
    }

    private fun setOnClickListeners(){
        binding.addOrEditButton.setOnClickListener{
            if(bookViewModel.updateRequested){
                val book = bookViewModel.bookToUpdate
                val passedData = BookListFragmentDirections.actionBookListFragmentToAddEditBookFragment(book)
                it.findNavController().navigate(passedData)
                bookViewModel.initUpdate(book) //required for go back action so that buttons are set properly
            }
            else{
                it.findNavController().navigate(R.id.action_bookListFragment_to_addEditBookFragment)
            }
        }

        binding.detailsButton.setOnClickListener {
            val book = bookViewModel.bookToUpdate
            val passedData = BookListFragmentDirections.actionBookListFragmentToBookDetails(book)
            it.findNavController().navigate(passedData)
            bookViewModel.initUpdate(book)
        }
    }

    private fun setSwipeToDelete(){
        val swipeToDelete = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                bookViewModel.delete(adapter.getBookAtPosition(position))
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recycleView)
    }

    private fun setSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null ){
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null ){
                    searchDatabase(query)
                }
                return true
            }
        })
    }

    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"

        bookViewModel.searchBooks(searchQuery).observe(viewLifecycleOwner) { list ->
            list.let {
                adapter.setData(it)
            }
        }
    }

    private fun setFilterChips(){

        val selectedChip = binding.chipGroup.children.filter {
            (it as Chip).isChecked
        }.map {
            (it as Chip).text.toString()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if(selectedChip.joinToString() == "All") {
                bookViewModel.getAllBooks().observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.setData(it)
                    }
                }
            }
            else{
                bookViewModel.getFilteredGenre(selectedChip.joinToString()).observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.setData(it)
                    }
                }
            }
        }
    }
}