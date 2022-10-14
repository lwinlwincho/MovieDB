package com.llc.moviebd.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.databinding.MovieListBinding

interface Delegate {
    fun onClickListener(model: MovieModel)
}

//You can use either listener or delegate.Delegate interface can override many fun and listener can acess one listener funciton..
class ItemAdapter(
    private val listener: (MovieModel) -> Unit,
    private val delegate: Delegate
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var movieList: List<MovieModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, delegate)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: MovieModel = movieList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    //for livedata observe
    fun setMovieList(setList: List<MovieModel>) {
        movieList = setList
        notifyDataSetChanged()
    }

    class ItemViewHolder(val binding: MovieListBinding, private val delegate: Delegate) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieModel) {
            binding.imgPoster.setImageResource(item.image)
            binding.txtMovieTitle.text = binding.root.context.getString(item.title)
            binding.txtMovieDate.text = binding.root.context.getString(item.date)

            binding.imgPoster.setOnClickListener {
                delegate.onClickListener(item)
            }
        }
    }
}

