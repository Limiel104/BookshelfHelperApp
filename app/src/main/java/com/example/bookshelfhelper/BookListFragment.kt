package com.example.bookshelfhelper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        //return super.onCreateView(inflater, container, savedInstanceState)
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

        initRecycleView()

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookListViewModelFactory(repository)
        bookViewModel = ViewModelProvider(this, factory)[BookListViewModel::class.java]
        binding.listViewModel = bookViewModel
        binding.lifecycleOwner = this

        displayBooksList()

        bookViewModel.isDone.observe(viewLifecycleOwner, Observer {
            binding.deleteButton.visibility = View.GONE
        })

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

        if(binding.deleteButton.visibility == View.GONE){
            binding.deleteButton.visibility = View.VISIBLE
        }
        else{
            binding.deleteButton.visibility = View.GONE
        }

        bookViewModel.initUpdate(book)
    }



}