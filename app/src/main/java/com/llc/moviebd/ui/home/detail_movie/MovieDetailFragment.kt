package com.llc.moviebd.ui.home.detail_movie

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.extension.toHourMinute
import com.llc.moviebd.network.IMAGE_URL
import com.llc.moviebd.singleEvent.observeEvent
import com.llc.moviebd.ui.home.cast.CastItemAdapter
import com.llc.moviebd.ui.home.genre.GenreItemAdapter
import com.llc.myinventory.database.MovieRoomDatabase

class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    private val appDatabase by lazy {
        MovieRoomDatabase.getDatabase(requireContext())
    }

    private val genreItemAdapter: GenreItemAdapter by lazy {
        GenreItemAdapter()
    }

    private val castItemAdapter: CastItemAdapter by lazy {
        CastItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId

        viewModel.getMovieDetail(movieId)
        viewModel.getCredits(movieId)
        viewModel.detailUIEvent.observe(viewLifecycleOwner) { detailResult ->
            when (detailResult) {
                is MovieDetailEvent.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is MovieDetailEvent.Success -> {
                    binding.detailScrollView.visibility = View.VISIBLE
                    bindDetailMovie(detailResult.movieDetailModel)
                    binding.detailProgressBar.visibility = View.GONE
                }
                is MovieDetailEvent.Error -> {
                    binding.detailProgressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        detailResult.error,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is MovieDetailEvent.Credits -> {
                    castItemAdapter.submitList(detailResult.creditModel.cast)
                }
            }
        }

        viewModel.favouriteAddEvent.observeEvent(viewLifecycleOwner) { favouriteEvent ->
            when (favouriteEvent) {
                is MovieDetailEvent.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is MovieDetailEvent.SuccessAdded -> {
                    if (favouriteEvent.message.isNotBlank()) {
                        Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    binding.detailProgressBar.visibility = View.GONE
                }
                is MovieDetailEvent.SuccessRemoved -> {
                    Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                        .show()
                    binding.detailProgressBar.visibility = View.GONE
                }
                is MovieDetailEvent.Error -> {
                    Toast.makeText(requireContext(), favouriteEvent.error, Toast.LENGTH_LONG)
                        .show()
                    binding.detailProgressBar.visibility = View.GONE
                }
                else -> {}
            }
        }

        binding.rvGenres.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = genreItemAdapter
        }

        binding.rvCasts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castItemAdapter
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bindDetailMovie(detailDataModel: MovieDetailModel) {

        //use this code in extension function
        /* Glide.with(requireContext())
             .load(IMAGE_URL + detailDataModel.backdrop_path)
             .transition(DrawableTransitionOptions.withCrossFade())
             .into(binding.ivDetail)*/

        with(binding) {
            ivDetail.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
            tvDetailName.text = detailDataModel.original_title
            //using sample function
            //tvLength.text = hourMinute(detailDataModel.runtime) Using sample fun
            //using extension function
            tvLength.text = detailDataModel.runtime.toHourMinute()
            tvRating.text = (detailDataModel.vote_average / 2).toString()
            tvDescription.text = detailDataModel.overview

            tvDetailStarRate.text =
                root.context.getString(
                    R.string.vote_average_format,
                    detailDataModel.vote_average.toString()
                )

            tvLanguage.text =
                if (detailDataModel.original_language == "en") "English"
                else if (detailDataModel.original_language == "ko") "Korea"
                else if (detailDataModel.original_language == "ja") "Japan"
                else if (detailDataModel.original_language == "fr") "France"
                else if (detailDataModel.original_language == "ch") "China"
                else detailDataModel.original_language

            genreItemAdapter.submitList(detailDataModel.genres)

            //var add=getClickable()

            bookMark.setOnClickListener {

                 // val color = resources.getColor(R.color.lavender)
                // bookMark.setBackgroundColor(color)
                //putBackground(color)
                /*  putClickable(add)

                  if (viewModel.isAdd(detailDataModel)) {
                      val color = resources.getColor(R.color.light_purple)
                      bookMark.setBackgroundColor(color)
                      putBackground(color)
                      add =getBackground()+1
                      addFav(detailDataModel)
                  } else {
                      val color = resources.getColor(R.color.white)
                      bookMark.setBackgroundColor(color)
                      putBackground(color)
                      deleteFav(detailDataModel)
                  }*/
                if(detailDataModel.title.equals(appDatabase.movieDao().getByTitle(detailDataModel.title))){
                    removeFav(detailDataModel)
                }else
                addFav(detailDataModel)
            }
        }
    }

    private fun addFav(detailDataModel: MovieDetailModel) {
        binding.bookMark.setColorFilter(resources.getColor(R.color.lavender))
        viewModel.addFavourite(
            appDatabase = appDatabase,
            model = detailDataModel
        )
    }

    private fun removeFav(detailDataModel: MovieDetailModel) {
        binding.bookMark.setColorFilter(resources.getColor(R.color.black))
        viewModel.removeFavourite(
            appDatabase = appDatabase,
            item = detailDataModel
        )
    }

    /* private fun putBackground(color: Int) {
         sharePreference.edit().putInt(KEY_BACKGROUND_COLOR, color).apply()
     }

     private fun getBackground(): Int {
         return sharePreference.getInt(KEY_BACKGROUND_COLOR, 0)
     }

     private fun putClickable(click: Int) {
         sharePreference.edit().putInt(KEY_CLICKABLE, click).apply()
     }

     private fun getClickable(): Int {
         return sharePreference.getInt(KEY_CLICKABLE, 0)
     }*/
}