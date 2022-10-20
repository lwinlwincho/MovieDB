package com.llc.moviebd.data.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.ItemMovieBinding


interface FavouriteClickListener {
    fun onFavouritClicked(favouriteModel: MovieModel)

}

class FavouriteMovieAdapter(private val favouriteClickListener: FavouriteClickListener) :
    RecyclerView.Adapter<FavouriteMovieAdapter.FavouriteMovieViewHolder>() {

    private var favouriteMovieList: List<MovieModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMovieViewHolder {

        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavouriteMovieViewHolder(binding, favouriteClickListener)


      /*  val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list, parent, false)
        return FavouriteMovieViewHolder(binding,favouriteClickListener)
*/
    }

    override fun onBindViewHolder(holder: FavouriteMovieViewHolder, position: Int) {
        val favouriteItem: MovieModel = favouriteMovieList[position]
        return holder.favouriteBind(favouriteItem)
    }

    override fun getItemCount(): Int {
        return favouriteMovieList.size
    }

    /*  override fun getItemCount(): Int {
          return movieListFavourite.size
          notifyDataSetChanged()
      }
  */
    fun setFavouriteList(setFavouriteList: List<MovieModel>) {
        favouriteMovieList = setFavouriteList
        notifyDataSetChanged()
    }


    class FavouriteMovieViewHolder(
        private val binding: ItemMovieBinding,
        private val favouriteClickListener: FavouriteClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun favouriteBind(favouriteItem: MovieModel) {


            binding.ivFavoriteMovie.setOnClickListener{
                favouriteClickListener.onFavouritClicked(favouriteItem)
            }


        }

    }


}