package com.example.bookshelfhelper.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        initRecycleView()

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookListViewModelFactory(repository)
        bookViewModel = ViewModelProvider(this, factory)[BookListViewModel::class.java]
        binding.bookListViewModel = bookViewModel
        binding.lifecycleOwner = this

        displayBooksList()

        val swipeToDelete = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                bookViewModel.delete(adapter.getBookAtPosition(position))
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recycleView)

        return  binding.root
    }

    fun initRecycleView(){
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
        Toast.makeText(requireContext(), "selected item: ${book.title}",Toast.LENGTH_LONG).show()
        bookViewModel.initUpdate(book)
    }
}