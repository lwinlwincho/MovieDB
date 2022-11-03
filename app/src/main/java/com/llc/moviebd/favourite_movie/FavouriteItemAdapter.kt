package com.llc.moviebd.favourite_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.database.MovieEntity
import com.llc.moviebd.databinding.ItemNowShowingBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

class FavouriteItemAdapter(private val onItemClickListener: (MovieEntity) -> Unit) :
    ListAdapter<MovieEntity, FavouriteItemAdapter.FavouriteViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            ItemNowShowingBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val movieItem: MovieEntity = getItem(position)
        holder.bind(movieItem)
    }

    class FavouriteViewHolder(
        private var binding: ItemNowShowingBinding,
        private val onItemClickListener: (MovieEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieEntity: MovieEntity) {

            with(binding) {
                ivPoster.loadFromUrl(IMAGE_URL + movieEntity.posterPath)
                tvMovieName.text = movieEntity.title
                tvStarRate.text = binding.root.context.getString(
                    R.string.vote_average_format,
                    movieEntity.voteAverage.toString()
                )

                root.setOnClickListener {
                    onItemClickListener.invoke(movieEntity)
                }
            }
        }
    }

    companion object diffCallBack : DiffUtil.ItemCallback<MovieEntity>() {

        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
}