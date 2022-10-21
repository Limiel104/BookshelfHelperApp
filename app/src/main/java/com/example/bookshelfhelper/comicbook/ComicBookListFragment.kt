package com.example.bookshelfhelper.comicbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
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
import com.google.android.material.chip.Chip

class ComicBookListFragment : Fragment() {

    private lateinit var comicBookViewModel: ComicBookListViewModel
    private lateinit var binding: FragmentComicbookListBinding
    private lateinit var adapter: ComicBookListRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comicbook_list, container,false)

        initRecycleView()

        val dao = BookshelfDatabase.getInstance(requireContext()).comicBookDao
        val repository = ComicBookRepository(dao)
        val factory = ComicBookListViewModelFactory(repository)
        comicBookViewModel = ViewModelProvider(this, factory)[ComicBookListViewModel::class.java]
        binding.comicBookListViewModel = comicBookViewModel
        binding.lifecycleOwner = this

        setOnClickListeners()
        displayComicBooksList()
        setSwipeToDelete()
        setSearchView()
        setFilterChips()

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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun listItemClicked(comicBook: ComicBook){
        comicBookViewModel.initUpdate(comicBook)

        if(comicBookViewModel.updateRequested){
            binding.addOrEditButton.setImageDrawable(resources.getDrawable(R.drawable.ic_edit))
            binding.detailsButton.visibility = View.VISIBLE
        }
        else{
            binding.addOrEditButton.setImageDrawable(resources.getDrawable(R.drawable.ic_add))
            binding.detailsButton.visibility = View.GONE
        }
    }

    private fun setOnClickListeners(){
        binding.addOrEditButton.setOnClickListener{
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

        binding.detailsButton.setOnClickListener {
            val comicBook = comicBookViewModel.comicBookToUpdate
            val passedData = ComicBookListFragmentDirections.actionComicbookListFragmentToComicBookDetails(comicBook)
            it.findNavController().navigate(passedData)
            comicBookViewModel.initUpdate(comicBook)
        }
    }

    private fun setSwipeToDelete(){
        val swipeToDelete = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                comicBookViewModel.delete(adapter.getComicBookAtPosition(position))
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recycleViewCB)
    }

    private fun setSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null ){
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query != null ){
                    searchDatabase(query)
                }
                return true
            }
        })
    }

    private fun searchDatabase(query: String){
        val searchQuery = "%$query%"

        comicBookViewModel.searchComicBooks(searchQuery).observe(viewLifecycleOwner) { list ->
            list.let {
                adapter.setData(it)
            }
        }
    }

    private fun setFilterChips(){

        val selectedChip = binding.chipGroup.children.filter {
            (it as Chip).isChecked
        }.map {
            (it as Chip).text.toString()
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->

            if(selectedChip.joinToString() == "All") {
                comicBookViewModel.getAllComicBooks().observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.setData(it)
                    }
                }
            }
            else{
                comicBookViewModel.getFilteredGenre(selectedChip.joinToString()).observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.setData(it)
                    }
                }
            }
        }
    }
}