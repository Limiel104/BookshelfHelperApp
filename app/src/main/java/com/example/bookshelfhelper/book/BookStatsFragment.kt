package com.example.bookshelfhelper.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentBookStatsBinding

class BookStatsFragment : Fragment() {

    private lateinit var bookStatsViewModel: BookStatsViewModel
    private lateinit var binding: FragmentBookStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_stats, container, false)

        val dao = BookshelfDatabase.getInstance(requireContext()).bookDao
        val repository = BookRepository(dao)
        val factory = BookStatsViewModelFactory(repository)
        bookStatsViewModel = ViewModelProvider(this,factory)[BookStatsViewModel::class.java]
        binding.bookStatsViewModel = bookStatsViewModel
        binding.lifecycleOwner = this

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.bookStatsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookStatsFragment_to_comicBookStatsFragment)
        }
    }
}