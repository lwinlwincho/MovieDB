package com.llc.moviedb.ui.home.now_showing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.R
import com.llc.moviebd.databinding.ItemNowShowingBinding
import com.llc.moviebd.databinding.ItemPopularBinding
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.extension.loadFromUrl
import com.llc.moviedb.network.IMAGE_URL

class NowShowingItemAdapter(private val onItemClickListener: (MovieModel) -> Unit) :
    ListAdapter<MovieModel, NowShowingItemAdapter.NowShowingViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowShowingViewHolder {
        return NowShowingViewHolder(
            ItemNowShowingBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: NowShowingViewHolder, position: Int) {
        val movieItem: MovieModel = getItem(position)
        holder.bind(movieItem)
    }

    class NowShowingViewHolder(
        private var binding: ItemNowShowingBinding,
        private val onItemClickListener: (MovieModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
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

    companion object DiffCallBack : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }
}
