package com.example.bookshelfhelper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookshelfhelper.data.ShelfItemDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentListBinding

class ListFragment : Fragment(){

    private lateinit var bookViewModel: ListViewModel
    private lateinit var binding: FragmentListBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container,false)

        binding.addOrEditButton.setOnClickListener{
            //insted of passing with bundle its better to use ViewModel
            //val bundle = bundleOf("user_input" to binding.)
            it.findNavController().navigate(R.id.action_listFragment_to_addEditItemFragment)
        }

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

        val dao = ShelfItemDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = ListViewModelFactory(repository)
        bookViewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]
        binding.listViewModel = bookViewModel
        binding.lifecycleOwner = this

        bookViewModel.books.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", it.toString())
            binding.recycleView.adapter = ListRecycleViewAdapter(it)
        })

        return  binding.root

    }

}