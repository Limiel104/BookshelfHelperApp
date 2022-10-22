package com.example.bookshelfhelper.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.BookRepository
import com.example.bookshelfhelper.databinding.FragmentBookStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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

        bookStatsViewModel.booksToSet.observe(viewLifecycleOwner, Observer {
            bookStatsViewModel.setBooks(it)
            setAllBooksThroughoutTheYearsBarChart()
            setAllAuthorsPieChart()
        })

        return binding.root
    }

    private fun setOnClickListeners(){
        binding.bookStatsButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookStatsFragment_to_comicBookStatsFragment)
        }
    }

    private fun setAllBooksThroughoutTheYearsBarChart(){
        val xLabels = bookStatsViewModel.getLabelsForBooksThroughoutTheYearsBarChart()

        val xAxis = binding.booksThroughoutTheYearsBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.granularity = 1F
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

        binding.booksThroughoutTheYearsBarChart.data = bookStatsViewModel.getDataForBooksThroughoutTheYearsBarChart()
        binding.booksThroughoutTheYearsBarChart.animateXY(2000,2000)
        binding.booksThroughoutTheYearsBarChart.description.isEnabled = false
    }

    private fun setAllAuthorsPieChart(){
        binding.authorsPieChart.data = bookStatsViewModel.getDataForAuthorsPieChart()
        binding.authorsPieChart.description.isEnabled = false
        binding.authorsPieChart.animateXY(2000,2000)
    }
}