package com.example.bookshelfhelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.databinding.ListItemBinding

class ListRecycleViewAdapter(private val booksList: List<Book>) : RecyclerView.Adapter<ListViewHolder>(){

    val te = Book(1,"dasagdfg","ased","asd","asd","sad","asd")
    val te2 = Book(2,"dweqweagasa","areagsd","asd","asd","sad","asd")
    val te3 = Book(2,"daagdfgssa","acxvbsd","asd","asd","sad","asd")
    val te4 = Book(2,"dadgsfsa","assbdd","asd","asd","sad","asd")
    val te5 = Book(2,"daase3q4sa","as bvd","asd","asd","sad","asd")

    //val a = ComicBook(1,23,23,false,"23232","23232","fff","ffff","ffff","ffff")

    val templist = listOf(te,te2,te3,te4,te5)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(booksList[position] )
        //holder.bind(templist[position])
    }

    override fun getItemCount(): Int {
        return booksList.size
        //return 5
        //return templist.size
    }

}

class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(book: Book){
        binding.itemTitle.text = book.title
        binding.itemAuthor.text = book.author
        binding.itemPublisher.text = book.publisher
    }
}