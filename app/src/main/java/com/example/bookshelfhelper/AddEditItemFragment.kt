package com.example.bookshelfhelper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.bookshelfhelper.data.ShelfItemDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentAddEditItemBinding

class AddEditItemFragment : Fragment(){

    private lateinit var addEditItemViewModel: AddEditItemViewModel
    private lateinit var binding: FragmentAddEditItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_item, container,false)
        //return super.onCreateView(inflater, container, savedInstanceState)

        //potrzebne od dodania
        val dao = ShelfItemDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = AddEditViewModelFactory(repository)
        addEditItemViewModel = ViewModelProvider(this, factory)[AddEditItemViewModel::class.java]
        binding.addEditItemViewModel = addEditItemViewModel
        binding.lifecycleOwner = this

//        binding.saveButton.setOnClickListener {
//            Log.i("TAG",it.toString())
//            Log.i("TAG","eloelo")
//            it.findNavController().navigate(R.id.action_addEditItemFragment_to_listFragment)
//        }

        return  binding.root
    }

}