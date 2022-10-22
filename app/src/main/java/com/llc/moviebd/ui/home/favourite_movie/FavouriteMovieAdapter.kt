package com.llc.moviebd.ui.home.favourite_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter
import com.llc.moviebd.databinding.ItemMovieBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL


interface FavouriteClickListener {
    fun onFavouritClicked(favouriteModel: MovieModel)

}

class FavouriteMovieAdapter(private val favouriteClickListener: FavouriteClickListener) :
    ListAdapter<MovieModel, FavouriteMovieAdapter.FavouriteMovieViewHolder>(NowShowingItemAdapter.DiffCallBack) {

    private var favouriteMovieList: List<MovieModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMovieViewHolder {

        return FavouriteMovieViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteMovieViewHolder, position: Int) {
        val favouriteItem: MovieModel = favouriteMovieList[position]
        holder.favouriteBind(favouriteItem, favouriteClickListener)
    }

    /*  override fun getItemCount(): Int {
          return movieListFavourite.size
          notifyDataSetChanged()
      }
  */
   /* fun setFavouriteList(setFavouriteList: List<MovieModel>) {
        favouriteMovieList = setFavouriteList
        notifyDataSetChanged()
    }*/

    class FavouriteMovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun favouriteBind(
            favouriteItem: MovieModel,
            favouriteClickListener: FavouriteClickListener
        ) {

            binding.imgPoster.loadFromUrl(IMAGE_URL+favouriteItem.posterPath)
            binding.txtPosterTitle.text=favouriteItem.title
            binding.txtPosterDate.text=favouriteItem.releaseDate
            binding.ivFavoriteMovie.setOnClickListener {
                favouriteClickListener.onFavouritClicked(favouriteItem)
            }


        }

    }


}