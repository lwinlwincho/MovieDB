package com.llc.moviebd.ui.home.seeMore

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

class SeeMoreItemAdapter(private val onItemClickListener:(MovieModel)->Unit) :
    ListAdapter<MovieModel, SeeMoreItemAdapter.SeeMoreViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeMoreViewHolder {
        return SeeMoreViewHolder(
            ItemNowShowingBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: SeeMoreViewHolder, position: Int) {
        val movieItem: MovieModel = getItem(position)
        holder.bind(movieItem)
    }

    class SeeMoreViewHolder(
        private var binding: ItemNowShowingBinding,
        private val onItemClickListener:(MovieModel)->Unit
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

    companion object diffCallBack : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }
}