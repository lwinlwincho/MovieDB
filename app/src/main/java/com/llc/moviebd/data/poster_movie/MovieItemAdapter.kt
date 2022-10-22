package com.llc.moviebd.data.poster_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.ItemMovieBinding
import com.llc.moviebd.databinding.ItemMovieShowingBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

interface Delegate {
    fun onClicklistener(movieModel: MovieModel)
}

class MovieItemAdapter(private val delegate: Delegate) :
    ListAdapter<MovieModel, MovieItemAdapter.MovieViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMovieShowingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieItemPosition: MovieModel = getItem(position)
        holder.bind(movieItemPosition, delegate)
    }

    /*private var movieListPoster: List<MovieModel>=emptyList()
      fun setMovieList(setList: List<MovieModel>) {
        movieListPoster = setList
        notifyDataSetChanged()
    }*/

    class MovieViewHolder(private var binding: ItemMovieShowingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieModel: MovieModel, delegate: Delegate) {

            binding.ivPoster.loadFromUrl(IMAGE_URL + movieModel.posterPath)
            binding.tvMovieName.text = movieModel.title

            binding.ivPoster.setOnClickListener() {
                delegate.onClicklistener(movieModel)
            }
        }
    }
}