package com.example.bookshelfhelper.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentBookDetailsBinding

class BookDetailsFragment : Fragment() {

    private lateinit var bookDetailsViewModel: BookDetailsViewModel
    private lateinit var binding: FragmentBookDetailsBinding
    private val passedData: BookDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_details, container, false)

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookDetailsViewModelFactory(repository)
        bookDetailsViewModel = ViewModelProvider(this,factory)[BookDetailsViewModel::class.java]
        binding.bookDetailsItemViewModel = bookDetailsViewModel
        binding.lifecycleOwner = this

        bookDetailsViewModel.bookToDisplay = passedData.bookToDisplay

        return binding.root
    }
}