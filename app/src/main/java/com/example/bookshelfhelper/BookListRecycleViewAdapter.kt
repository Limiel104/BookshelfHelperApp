package com.example.bookshelfhelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.databinding.BookListItemBinding

class ListRecycleViewAdapter(private val booksList: List<Book>,
                             private val selectedItem:(Book)->Unit)
    : RecyclerView.Adapter<ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : BookListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.book_list_item, parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(booksList[position],selectedItem)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }
}

class ListViewHolder(val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(book: Book, selectedItem:(Book)->Unit){
        binding.itemTitle.text = book.title
        binding.itemAuthor.text = book.author
        binding.itemPublisher.text = book.publisher
        //binding.bookItemLayout.background.setTint()

        binding.bookItemLayout.setOnClickListener {
            selectedItem(book)
        }
    }
}