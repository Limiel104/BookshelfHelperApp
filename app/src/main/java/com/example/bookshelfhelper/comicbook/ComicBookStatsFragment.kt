package com.example.bookshelfhelper.comicbook

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
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentComicBookStatsBinding

class ComicBookStatsFragment : Fragment() {

    private lateinit var comicBookStatsViewModel: ComicBookStatsViewModel
    private lateinit var binding: FragmentComicBookStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comic_book_stats, container, false)

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = ComicBookStatsViewModelFactory(repository)
        comicBookStatsViewModel = ViewModelProvider(this,factory)[ComicBookStatsViewModel::class.java]
        binding.comicBookStatsViewModel = comicBookStatsViewModel
        binding.lifecycleOwner = this

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.comicBookStatsButton.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

}