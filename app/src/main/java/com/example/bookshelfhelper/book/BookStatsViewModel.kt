package com.example.bookshelfhelper.book

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.repository.BookRepository
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

class BookStatsViewModel(private val repository: BookRepository) : ViewModel() {

    val booksToSet = repository.books
    private lateinit var books: ArrayList<Book>
    private lateinit var pieList: ArrayList<PieEntry>
    private lateinit var pieDataSet: PieDataSet
    private lateinit var pieData: PieData
    private lateinit var barList: ArrayList<BarEntry>
    private lateinit var barDataSet: BarDataSet
    private lateinit var barData: BarData
    var inputTitle = MutableLiveData<String>()
    var inputOption = MutableLiveData<String>()

    init {
        inputTitle.value = ""
        Log.i("TAG","BookStatsViewModel")
    }

    fun setBooks(booksToSet: List<Book>){
        books = booksToSet as ArrayList<Book>
    }

    fun setChartTitle(title: String){
        inputTitle.value = title
    }

    fun getDataForBooksThroughoutTheYearsBarChart(): BarData{
        var years = getLabelsForBooksThroughoutTheYearsBarChart()
        var linkedHashMap = LinkedHashMap<String,Float>()

        for(year in years){
            linkedHashMap[year] = 0f
        }

        for(book in books){
            linkedHashMap[book.yearBought.toString()] = linkedHashMap[book.yearBought.toString()]!! + 1
        }

        barList = ArrayList()

        var i = 0f
        for(hash in linkedHashMap){
            barList.add(BarEntry(i,hash.value))
            i++
        }

        barDataSet = BarDataSet(barList,("Years"))
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS,250)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f
        barData = BarData(barDataSet)

        return barData
    }

    fun getLabelsForBooksThroughoutTheYearsBarChart(): ArrayList<String>{
        var yearsInt = ArrayList<Int>()
        var years = ArrayList<String>()

        for(book in books){
            if(!yearsInt.contains(book.yearBought)){
                yearsInt.add(book.yearBought)
            }
        }

        yearsInt.sort()

        for(year in yearsInt) {
            years.add(year.toString())
        }

        return years
    }

    fun getDataForAuthorsPieChart(): PieData{
        var names = ArrayList<String>()
        var hashMap = HashMap<String,Float>()

        for(book in books){
            if(!names.contains(book.author)){
                names.add(book.author)
            }
        }

        for(name in names){
            hashMap[name] = 0f
        }

        for(book in books){
            if(hashMap.containsKey(book.author))
                hashMap[book.author] = hashMap[book.author]!! + 1
        }

        pieList = ArrayList()

        for(hash in hashMap){
            pieList.add(PieEntry(hash.value,hash.key))
        }

        pieDataSet = PieDataSet(pieList,"Authors")
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS,250)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f
        pieData = PieData(pieDataSet)

        return pieData
    }
}