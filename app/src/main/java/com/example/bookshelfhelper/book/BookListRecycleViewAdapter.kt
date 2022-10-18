package com.example.bookshelfhelper.book

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.model.Book
import com.example.bookshelfhelper.databinding.BookListItemBinding

class BookListRecycleViewAdapter(private val selectedItem:(Book)->Unit)
    : RecyclerView.Adapter<BookListRecycleViewAdapter.ListViewHolder>(){

    private var booksList = ArrayList<Book>()
    var isItemSelected = false
    var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : BookListItemBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.book_list_item, parent,false)
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

    fun getBookAtPosition(position: Int): Book {
        return booksList[position]
    }

    fun setData(newList: List<Book>){
        booksList = newList as ArrayList<Book>
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: BookListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(book: Book, selectedItem:(Book)->Unit){
            binding.itemTitle.text = book.title
            binding.itemAuthor.text = book.author
            binding.itemPublisher.text = book.publisher

            val text = book.format + " | " + book.type + " | " + book.language
            binding.itemFTL.text = text

            Glide.with(binding.itemImg.context)
                .load(book.imagePath)
                .into(binding.itemImg)

            if(checkedPosition == -1) {
                binding.bookItemLayout.setCardBackgroundColor(Color.WHITE)
            }
            else {
                if(checkedPosition == adapterPosition) {
                    binding.bookItemLayout.setCardBackgroundColor(Color.GRAY)
                }
                else {
                    binding.bookItemLayout.setCardBackgroundColor(Color.WHITE)
                }
            }

            binding.bookItemLayout.setOnClickListener {

                if(isItemSelected){
                    if(checkedPosition == adapterPosition){
                        binding.bookItemLayout.setCardBackgroundColor(Color.WHITE)
                        isItemSelected = false
                        selectedItem(book)
                    }
                }
                else{
                    binding.bookItemLayout.setCardBackgroundColor(Color.GRAY)

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


