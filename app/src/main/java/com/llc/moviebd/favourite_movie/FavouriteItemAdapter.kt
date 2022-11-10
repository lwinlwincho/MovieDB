package com.llc.moviebd.favourite_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.database.FavouriteMovieEntity
import com.llc.moviebd.databinding.ItemNowShowingBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

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
        fun bind(favouriteMovieEntity: FavouriteMovieEntity) = with(binding) {

            ivPoster.loadFromUrl(IMAGE_URL + favouriteMovieEntity.posterPath)
            tvMovieName.text = favouriteMovieEntity.title
            tvStarRate.text = binding.root.context.getString(
                R.string.vote_average_format,
                favouriteMovieEntity.voteAverage
            )

            root.setOnClickListener {
                onItemClickListener.invoke(favouriteMovieEntity)
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