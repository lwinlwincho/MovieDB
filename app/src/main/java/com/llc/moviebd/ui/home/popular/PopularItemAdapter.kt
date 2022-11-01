package com.llc.moviebd.ui.home.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.ItemPopularBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

class PopularItemAdapter(private val onItemClickListener: (MovieModel) -> Unit) :
    ListAdapter<MovieModel, PopularItemAdapter.PopularMovieViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        return PopularMovieViewHolder(
            ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        val movieItem: MovieModel = getItem(position)
        holder.bind(movieItem)
    }

    class PopularMovieViewHolder(
        private var binding: ItemPopularBinding,
        private val onItemClickListener: (MovieModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieModel: MovieModel) {

            with(binding) {
                ivPoster.loadFromUrl(IMAGE_URL + movieModel.posterPath)
                tvMovieName.text = movieModel.title
                tvStarRate.text = binding.root.context.getString(
                    R.string.vote_average_format,
                    movieModel.vote_average.toString()
                )

                root.setOnClickListener {
                    onItemClickListener.invoke(movieModel)
                }
            }
        }
    }

    companion object diffCallback : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }
}