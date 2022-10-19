package com.example.bookshelfhelper.comicbook

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelfhelper.R
import com.example.bookshelfhelper.data.model.ComicBook
import com.example.bookshelfhelper.databinding.ComicbookListItemBinding

class ComicBookListRecycleViewAdapter(private val selectedItem:(ComicBook)->Unit)
    : RecyclerView.Adapter<ComicBookListRecycleViewAdapter.ListViewHolder>() {

    private var comicBooksList = ArrayList<ComicBook>()
    var isItemSelected = false
    var checkedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ComicbookListItemBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.comicbook_list_item, parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(comicBooksList[position],selectedItem)
    }

    override fun getItemCount(): Int {
        return comicBooksList.size
    }

    fun setList(comicBooks: List<ComicBook>){
        comicBooksList.clear()
        comicBooksList.addAll(comicBooks)
    }

    fun getComicBookAtPosition(position: Int): ComicBook {
        return comicBooksList[position]
    }

    fun setData(newList: List<ComicBook>){
        comicBooksList = newList as ArrayList<ComicBook>
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: ComicbookListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(comicBook: ComicBook, selectedItem:(ComicBook)->Unit){
            binding.itemTitle.text = comicBook.title
            binding.itemAuthor.text = comicBook.author
            binding.itemPublisher.text = comicBook.publisher

            val text = comicBook.format + " | " + comicBook.type + " | " + comicBook.language + " | " + comicBook.genre
            binding.itemFTL.text = text

            val text2 = comicBook.pagesColor + " | " + comicBook.volumesOwned + "/" + comicBook.volumesReleased
            binding.itemPCVOVR.text = text2

            Glide.with(binding.itemImg.context)
                .load(comicBook.imagePath)
                .into(binding.itemImg)

            if(checkedPosition == -1) {
                binding.comicBookItemLayout.setCardBackgroundColor(Color.WHITE)
            }
            else {
                if(checkedPosition == adapterPosition) {
                    binding.comicBookItemLayout.setCardBackgroundColor(Color.GRAY)
                }
                else {
                    binding.comicBookItemLayout.setCardBackgroundColor(Color.WHITE)
                }
            }

            binding.comicBookItemLayout.setOnClickListener {

                if(isItemSelected){
                    if(checkedPosition == adapterPosition){
                        binding.comicBookItemLayout.setCardBackgroundColor(Color.WHITE)
                        isItemSelected = false
                        selectedItem(comicBook)
                    }
                }
                else{
                    binding.comicBookItemLayout.setCardBackgroundColor(Color.GRAY)

                    if(checkedPosition != adapterPosition){
                        notifyItemChanged(checkedPosition)
                        checkedPosition = adapterPosition
                    }

                    isItemSelected = true
                    selectedItem(comicBook)
                }
            }
        }
    }
}