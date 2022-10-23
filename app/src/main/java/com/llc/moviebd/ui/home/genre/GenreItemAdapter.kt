package com.llc.moviebd.ui.home.genre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.data.model.Genre
import com.llc.moviebd.databinding.ItemGenresBinding

class GenreItemAdapter : ListAdapter<Genre, GenreItemAdapter.GenresViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Genre>() {

        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        return GenresViewHolder(ItemGenresBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val item: Genre = getItem(position)
        holder.bind(item)
    }

    class GenresViewHolder(private val binding: ItemGenresBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
           // binding.tvGenres.text = genre.name
        }
    }
}