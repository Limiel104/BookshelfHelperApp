package com.example.bookshelfhelper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentBookListBinding

class BookListFragment : Fragment(){

    private lateinit var bookViewModel: BookListViewModel
    private lateinit var binding: FragmentBookListBinding

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val binding = FragmentListBinding.bind(view)
//
//        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())
//
//        val dao = ShelfItemDatabase.getInstance(requireContext()).bookDao
//        val repository = BookRepository(dao)
//        val factory = ListViewModelFactory(repository)
//        bookViewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]
//        binding.listViewModel = bookViewModel
//        binding.lifecycleOwner = this
//
//        bookViewModel.books.observe(viewLifecycleOwner, Observer {
//            Log.i("TAG", it.toString())
//            binding.recycleView.adapter = ListRecycleViewAdapter(it)
//        })
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        //inflate layout with this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container,false)

        binding.addOrEditButton.setOnClickListener{
            //insted of passing with bundle its better to use ViewModel
            //val bundle = bundleOf("user_input" to binding.)
            it.findNavController().navigate(R.id.action_bookListFragment_to_addEditBookFragment)
        }

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookListViewModelFactory(repository)
        bookViewModel = ViewModelProvider(this, factory)[BookListViewModel::class.java]
        binding.listViewModel = bookViewModel
        binding.lifecycleOwner = this

        bookViewModel.books.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", it.toString())
            Log.i("TAG","eloeo")
            binding.recycleView.adapter = ListRecycleViewAdapter(it)
        })

        return  binding.root

    }

}