package com.example.bookshelfhelper.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentAddEditBookBinding

class AddEditBookFragment : Fragment(){

    private lateinit var addEditBookItemViewModel: AddEditBookViewModel
    private lateinit var binding: FragmentAddEditBookBinding
    private val passedData: AddEditBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_book, container,false)

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = AddEditBookViewModelFactory(repository)
        addEditBookItemViewModel = ViewModelProvider(this, factory)[AddEditBookViewModel::class.java]
        binding.addEditBookItemViewModel = addEditBookItemViewModel
        binding.lifecycleOwner = this

        if(passedData.bookToUpdate != null){
            passedData.bookToUpdate?.let { Log.i("TAG", it.title.toString()) }
            addEditBookItemViewModel.bookToUpdate = passedData.bookToUpdate!!
            addEditBookItemViewModel.prepareUpdateLayout()
            addEditBookItemViewModel.updateMode = true
        }

        addEditBookItemViewModel.isDone.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        return  binding.root
    }
}