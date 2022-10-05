package com.example.bookshelfhelper

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.databinding.BookListItemBinding

class BookListRecycleViewAdapter(private val selectedItem:(Book)->Unit)
    : RecyclerView.Adapter<BookListRecycleViewAdapter.ListViewHolder>(){

    private val booksList = ArrayList<Book>()
    var isItemSelected = false
    var checkedPosition = -1

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

    fun setList(books: List<Book>){
        booksList.clear()
        booksList.addAll(books)
    }

    inner class ListViewHolder(val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(book: Book, selectedItem:(Book)->Unit){
            binding.itemTitle.text = book.title
            binding.itemAuthor.text = book.author
            binding.itemPublisher.text = book.publisher

            if(checkedPosition == -1) {
                binding.bookItemLayout.setBackgroundColor(Color.WHITE)
            }
            else {
                if(checkedPosition == adapterPosition) {
                    binding.bookItemLayout.setBackgroundColor(Color.BLUE)
                }
                else {
                    binding.bookItemLayout.setBackgroundColor(Color.WHITE)
                }
            }

            binding.bookItemLayout.setOnClickListener {

                if(isItemSelected){
                    if(checkedPosition == adapterPosition){
                        binding.bookItemLayout.setBackgroundColor(Color.WHITE)
                        isItemSelected = false
                        selectedItem(book)
                    }
                }
                else{
                    binding.bookItemLayout.setBackgroundColor(Color.BLUE)

                    if(checkedPosition != adapterPosition){
                        notifyItemChanged(checkedPosition)
                        checkedPosition = adapterPosition
                    }

                    isItemSelected = true
                    selectedItem(book)
                }
            }
        }
    }


}


