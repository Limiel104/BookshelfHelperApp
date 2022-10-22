package com.example.bookshelfhelper.comicbook

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.data.repository.ComicBookRepository
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

class ComicBookStatsViewModel(private val repository: ComicBookRepository) : ViewModel() {

    val comicBooksToSet = repository.comicBooks
    private lateinit var comicBooks: ArrayList<ComicBook>
    private lateinit var piePublishersList: ArrayList<PieEntry>
    private lateinit var piePublishersDataSet: PieDataSet
    private lateinit var piePublishersData: PieData
    private lateinit var pieTitlesList: ArrayList<PieEntry>
    private lateinit var pieTitlesDataSet: PieDataSet
    private lateinit var pieTitlesData: PieData
    private lateinit var barStatusesList: ArrayList<BarEntry>
    private lateinit var barStatusesDataSet: BarDataSet
    private lateinit var barStatusesData: BarData

    init {
        Log.i("TAG","ComicBookStatsViewModel")
    }

    fun setComicBooks(comicBooksToSet: List<ComicBook>){
        comicBooks = comicBooksToSet as ArrayList<ComicBook>
    }

    fun getDataForPublishersPieChart(): PieData{
        var names = ArrayList<String>()
        var hashMap = HashMap<String,Float>()

        for(comicBook in comicBooks){
            if(!names.contains(comicBook.publisher)){
                names.add(comicBook.publisher)
            }
        }

        for(name in names){
            hashMap[name] = 0f
        }

        for(comicBook in comicBooks){
            if(hashMap.containsKey(comicBook.publisher)){
                hashMap[comicBook.publisher] = hashMap[comicBook.publisher]!! + 1
            }
        }

        piePublishersList = ArrayList()

        for(hash in hashMap){
            piePublishersList.add(PieEntry(hash.value,hash.key))
        }

        piePublishersDataSet = PieDataSet(piePublishersList,"Publishers")
        piePublishersDataSet.setColors(ColorTemplate.JOYFUL_COLORS,250)
        piePublishersDataSet.valueTextColor = Color.BLACK
        piePublishersDataSet.valueTextSize = 15f
        piePublishersData = PieData(piePublishersDataSet)

        return piePublishersData
    }

    fun getDataForTitlesPieChart(): PieData{
        var hashMap = HashMap<String,Float>()

        for(comicBook in comicBooks){
            if(!hashMap.containsKey(comicBook.title)){
                hashMap[comicBook.title] = comicBook.volumesOwned * 1f
            }
        }

        pieTitlesList = ArrayList()

        for(hash in hashMap){
            pieTitlesList.add(PieEntry(hash.value,hash.key))
        }

        pieTitlesDataSet = PieDataSet(pieTitlesList,"Titles")
        pieTitlesDataSet.setColors(ColorTemplate.JOYFUL_COLORS,250)
        pieTitlesDataSet.valueTextColor = Color.BLACK
        pieTitlesDataSet.valueTextSize = 15f
        pieTitlesData = PieData(pieTitlesDataSet)

        return pieTitlesData
    }

    fun getLabelsForStatusesBarChart(): ArrayList<String>{
        var statuses = ArrayList<String>()

        for(comicBook in comicBooks){
            if(!statuses.contains(comicBook.status)){
                statuses.add(comicBook.status)
            }
        }

        return statuses
    }

    fun getDataForStatusesBarChart(): BarData{
        var statuses = getLabelsForStatusesBarChart()
        var linkedHashMap = LinkedHashMap<String,Float>()

        for(status in statuses){
            linkedHashMap[status] = 0f
        }

        for(comicBook in comicBooks){
            linkedHashMap[comicBook.status] = linkedHashMap[comicBook.status]!! + 1
        }

        barStatusesList = ArrayList()

        var i = 0f
        for(hash in linkedHashMap){
            barStatusesList.add(BarEntry(i,hash.value))
            i++
        }

        barStatusesDataSet = BarDataSet(barStatusesList,"Statuses")
        barStatusesDataSet.setColors(ColorTemplate.JOYFUL_COLORS,250)
        barStatusesDataSet.valueTextColor = Color.BLACK
        barStatusesDataSet.valueTextSize = 15f
        barStatusesData = BarData(barStatusesDataSet)

        return barStatusesData
    }
}