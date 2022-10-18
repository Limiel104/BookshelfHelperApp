package com.example.bookshelfhelper.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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

    private fun listItemClicked(book: Book){
        bookViewModel.initUpdate(book)
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

        binding.comicbooksButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookListFragment_to_comicbookListFragment)
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

        binding.chip1.setOnClickListener{
            Toast.makeText(context,"All Books",Toast.LENGTH_LONG).show()
            bookViewModel.getAllBooks().observe(viewLifecycleOwner) { list ->
                list.let {
                    adapter.setData(it)
                }
            }
        }

        binding.chip2.setOnClickListener{
            Toast.makeText(context,"Hard Cover",Toast.LENGTH_LONG).show()
            bookViewModel.getFilteredType("Hard Cover").observe(viewLifecycleOwner) { list ->
                list.let {
                    adapter.setData(it)
                }
            }
        }

        binding.chip3.setOnClickListener{
            Toast.makeText(context,"Soft Cover",Toast.LENGTH_LONG).show()
            bookViewModel.getFilteredType("Soft Cover").observe(viewLifecycleOwner) { list ->
                list.let {
                    adapter.setData(it)
                }
            }
        }
    }
}