package com.llc.moviebd.ui.home.now_showing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.ItemNowShowingBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

interface Delegate {
    fun onClicklistener(movieModel: MovieModel)
}

class NowShowingItemAdapter(private val delegate: Delegate) :
    ListAdapter<MovieModel, NowShowingItemAdapter.NowShowingViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowShowingViewHolder {
        return NowShowingViewHolder(ItemNowShowingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NowShowingViewHolder, position: Int) {
        val movieItemPosition: MovieModel = getItem(position)
        holder.bind(movieItemPosition, delegate)
    }

    class NowShowingViewHolder(private var binding: ItemNowShowingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieModel: MovieModel, delegate: Delegate) {

            binding.ivPoster.loadFromUrl(IMAGE_URL + movieModel.posterPath)
            binding.tvMovieName.text = movieModel.title
            binding.tvStarRate.text = binding.root.context.getString(
                R.string.vote_average_format,
                movieModel.vote_average.toString()
            )

            binding.ivPoster.setOnClickListener() {
                delegate.onClicklistener(movieModel)
            }
        }
    }
}