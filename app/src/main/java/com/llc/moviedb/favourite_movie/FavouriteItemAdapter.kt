package com.llc.moviedb.favourite_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.databinding.ItemNowShowingBinding
import com.llc.moviedb.database.FavouriteMovieEntity
import com.llc.moviedb.extension.loadFromUrl
import com.llc.moviedb.network.IMAGE_URL

class FavouriteItemAdapter(private val onItemClickListener: (FavouriteMovieEntity) -> Unit) :
    ListAdapter<FavouriteMovieEntity, FavouriteItemAdapter.FavouriteViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            ItemNowShowingBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val movieItem: FavouriteMovieEntity = getItem(position)
        holder.bind(movieItem)
    }

    class FavouriteViewHolder(
        private var binding: ItemNowShowingBinding,
        private val onItemClickListener: (FavouriteMovieEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favouriteMovie: FavouriteMovieEntity) = with(binding) {

            ivPoster.loadFromUrl(IMAGE_URL + favouriteMovie.posterPath)
            tvMovieName.text = favouriteMovie.title
            tvStarRate.text = binding.root.context.getString(
                R.string.vote_average_format,
                favouriteMovie.voteAverage
            )

            root.setOnClickListener {
                onItemClickListener.invoke(favouriteMovie)
            }
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<FavouriteMovieEntity>() {

        override fun areItemsTheSame(
            oldItem: FavouriteMovieEntity,
            newItem: FavouriteMovieEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FavouriteMovieEntity,
            newItem: FavouriteMovieEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}