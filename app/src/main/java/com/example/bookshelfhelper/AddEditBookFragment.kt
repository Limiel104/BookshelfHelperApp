package com.example.bookshelfhelper

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentAddEditBookBinding

class AddEditBookFragment : Fragment(){

    private lateinit var addEditItemViewModel: AddEditBookViewModel
    private lateinit var binding: FragmentAddEditBookBinding
    private val passedData: AddEditBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_book, container,false)
        //return super.onCreateView(inflater, container, savedInstanceState)

        //potrzebne od dodania
        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = AddEditBookViewModelFactory(repository)
        addEditItemViewModel = ViewModelProvider(this, factory)[AddEditBookViewModel::class.java]
        binding.addEditItemViewModel = addEditItemViewModel
        binding.lifecycleOwner = this

        if(passedData.bookToUpdate != null){
            passedData.bookToUpdate?.let { Log.i("TAG", it.title.toString()) }
            addEditItemViewModel.bookToUpdate = passedData.bookToUpdate!!
            addEditItemViewModel.prepareUpdateLayout()
            addEditItemViewModel.updateMode = true
        }

        addEditItemViewModel.isDone.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        return  binding.root
    }


}