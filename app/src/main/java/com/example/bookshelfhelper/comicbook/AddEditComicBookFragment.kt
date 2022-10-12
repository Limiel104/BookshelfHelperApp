package com.example.bookshelfhelper.comicbook

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentAddEditComicBookBinding

class AddEditComicBookFragment : Fragment() {

    private lateinit var addEditComicBookItemViewModel: AddEditComicBookViewModel
    private lateinit var binding: FragmentAddEditComicBookBinding
    private val passedData: AddEditComicBookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_comic_book, container,false)

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = AddEditComicBookViewModelFactory(repository)
        addEditComicBookItemViewModel = ViewModelProvider(this, factory)[AddEditComicBookViewModel::class.java]
        binding.addEditComicBookItemViewModel = addEditComicBookItemViewModel
        binding.lifecycleOwner = this

        prepareAutocompleteFields()

        if(passedData.comicBookToUpdate != null){
            passedData.comicBookToUpdate?.let { Log.i("TAG", it.title.toString()) }
            addEditComicBookItemViewModel.comicBookToUpdate = passedData.comicBookToUpdate!!
            addEditComicBookItemViewModel.prepareUpdateLayout()
            addEditComicBookItemViewModel.updateMode = true
        }

        addEditComicBookItemViewModel.isDone.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        return  binding.root
    }

    private fun prepareAutocompleteFields(){

        val formats = resources.getStringArray(R.array.formats)
        val types = resources.getStringArray(R.array.types)
        val languages = resources.getStringArray(R.array.languages)
        val pagesColor = resources.getStringArray(R.array.pagesColor)

        val  formatsArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,formats) }
        val  typesArrayAdapter = context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,types) }
        val  languagesArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,languages) }
        val  pagesColorArrayAdapter =
            context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,pagesColor) }

        binding.addFormat.setAdapter(formatsArrayAdapter)
        binding.addType.setAdapter(typesArrayAdapter)
        binding.addLanguage.setAdapter(languagesArrayAdapter)
        binding.addPagesColor.setAdapter(pagesColorArrayAdapter)
    }
}