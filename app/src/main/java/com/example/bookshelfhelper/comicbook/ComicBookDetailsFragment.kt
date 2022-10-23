package com.example.bookshelfhelper.comicbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentComicBookDetailsBinding

class ComicBookDetailsFragment : Fragment() {

    private lateinit var comicBookDetailsViewModel: ComicBookDetailsViewModel
    private lateinit var binding: FragmentComicBookDetailsBinding
    private val passedData: ComicBookDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_comic_book_details, container, false)

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = ComicBookDetailsViewModelFactory(repository)
        comicBookDetailsViewModel = ViewModelProvider(this,factory)[ComicBookDetailsViewModel::class.java]
        binding.comicBookDetailsItemViewModel = comicBookDetailsViewModel
        binding.lifecycleOwner = this

        comicBookDetailsViewModel.comicBookToDisplay = passedData.comicBookToDisplay
        comicBookDetailsViewModel.setLayout()
        Glide.with(binding.itemImg.context)
            .load(passedData.comicBookToDisplay.imagePath)
            .into(binding.itemImg)

        prepareObservers()

        return binding.root
    }

    private fun prepareObservers(){
        comicBookDetailsViewModel.message.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context,it, Toast.LENGTH_LONG).show()
            }
        })
    }
}