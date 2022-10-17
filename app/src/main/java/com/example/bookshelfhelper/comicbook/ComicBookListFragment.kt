package com.example.bookshelfhelper.comicbook

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.SwipeToDelete
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentComicbookListBinding

class ComicBookListFragment : Fragment() {

    private lateinit var comicBookViewModel: ComicBookListViewModel
    private lateinit var binding: FragmentComicbookListBinding
    private lateinit var adapter: ComicBookListRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_comicbook_list, container,false)

        binding.addOrEditButtonCB.setOnClickListener{
            if(comicBookViewModel.updateRequested){
                val comicBook = comicBookViewModel.comicBookToUpdate
                val passedData = ComicBookListFragmentDirections.actionComicbookListFragmentToAddEditComicBookFragment(comicBook)
                it.findNavController().navigate(passedData)
                comicBookViewModel.initUpdate(comicBook) //required for go back action so that buttons are set properly
            }
            else{
                it.findNavController().navigate(R.id.action_comicbookListFragment_to_addEditComicBookFragment)
            }
        }

        binding.booksButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_comicbookListFragment_to_bookListFragment)
        }

        initRecycleView()

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = ComicBookListViewModelFactory(repository)
        comicBookViewModel = ViewModelProvider(this, factory)[ComicBookListViewModel::class.java]
        binding.comicBookListViewModel = comicBookViewModel
        binding.lifecycleOwner = this

        displayComicBooksList()

        val swipeToDelete = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                comicBookViewModel.delete(adapter.getComicBookAtPosition(position))
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recycleViewCB)

        return  binding.root
    }

    private fun initRecycleView(){
        binding.recycleViewCB.layoutManager = LinearLayoutManager(requireContext())
        adapter = ComicBookListRecycleViewAdapter() { selectedItem: ComicBook ->
            listItemClicked(
                selectedItem
            )}
        binding.recycleViewCB.adapter = adapter
    }

    private fun displayComicBooksList(){
        comicBookViewModel.comicBooks.observe(viewLifecycleOwner, Observer {
            Log.i("TAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(comicBook: ComicBook){
        comicBookViewModel.initUpdate(comicBook)
    }
}