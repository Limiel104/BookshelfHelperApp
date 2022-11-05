package com.example.bookshelfhelper.comicbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.BookshelfDatabase
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.example.bookshelfhelper.databinding.FragmentComicBookStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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

        prepareAutocompleteFields()
        prepareObservers()

        return binding.root
    }

    private fun prepareAutocompleteFields(){
        val options = resources.getStringArray(R.array.comicBookStats)
        val optionsArrayAdapter = context?.let { ArrayAdapter(it,R.layout.drop_down_list_item,options) }
        binding.chooseChart.setAdapter(optionsArrayAdapter)
    }

    private fun prepareObservers(){
        comicBookStatsViewModel.comicBooksToSet.observe(viewLifecycleOwner, Observer {
            comicBookStatsViewModel.setComicBooks(it)
            setAllPublishersPieChart()
            setAllComicBooksPieChart()
            setAllStatusesBarChart()
        })

        comicBookStatsViewModel.inputOption.observe(viewLifecycleOwner, Observer {
            when (comicBookStatsViewModel.inputOption.value) {
                "Volumes Owned by Titles" -> {
                    comicBookStatsViewModel.setChartTitle(it)
                    binding.titlesPieChart.visibility = View.VISIBLE
                    binding.publishersPieChart.visibility = View.GONE
                    binding.statusBarChart.visibility = View.GONE
                }
                "Comic books by Publisher" -> {
                    comicBookStatsViewModel.setChartTitle(it)
                    binding.titlesPieChart.visibility = View.GONE
                    binding.publishersPieChart.visibility = View.VISIBLE
                    binding.statusBarChart.visibility = View.GONE
                }
                "Comic books by Status" -> {
                    comicBookStatsViewModel.setChartTitle(it)
                    binding.titlesPieChart.visibility = View.GONE
                    binding.publishersPieChart.visibility = View.GONE
                    binding.statusBarChart.visibility = View.VISIBLE
                }
                "Go to Book stats" -> {
                    comicBookStatsViewModel.inputOption.value = null
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun setAllPublishersPieChart(){
        binding.publishersPieChart.data = comicBookStatsViewModel.getDataForPublishersPieChart()
        binding.publishersPieChart.description.isEnabled = false
        binding.publishersPieChart.animateXY(2000,2000)
    }

    private fun setAllComicBooksPieChart(){
        binding.titlesPieChart.data = comicBookStatsViewModel.getDataForTitlesPieChart()
        binding.titlesPieChart.description.isEnabled = false
        binding.titlesPieChart.animateXY(2000,2000)
    }

    private fun setAllStatusesBarChart(){
        val xLabels = comicBookStatsViewModel.getLabelsForStatusesBarChart()

        val xAxis = binding.statusBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.granularity = 1F
        xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

        binding.statusBarChart.data = comicBookStatsViewModel.getDataForStatusesBarChart()
        binding.statusBarChart.animateXY(2000,2000)
        binding.statusBarChart.description.isEnabled = false
    }
}