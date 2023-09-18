package com.llc.moviedb.ui.home.detail_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.R
import com.llc.moviebd.databinding.FragmentMovieCollapseToolbarBinding
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviedb.data.model.MovieDetailModel
import com.llc.moviedb.extension.loadFromUrl
import com.llc.moviedb.extension.toHourMinute
import com.llc.moviedb.network.IMAGE_URL
import com.llc.moviedb.singleEvent.observeEvent
import com.llc.moviedb.ui.home.cast.CastItemAdapter
import com.llc.moviedb.ui.home.genre.GenreItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()

    private var _binding: FragmentMovieCollapseToolbarBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

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
        _binding = FragmentMovieCollapseToolbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        viewModel.getMovieDetail(movieId)

        viewModel.detailUIEvent.observe(viewLifecycleOwner) { detailResult ->
            when (detailResult) {
                is MovieDetailEvent.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is MovieDetailEvent.Success -> {
                   // binding.detailScrollView.visibility = View.VISIBLE
                    binding.detailProgressBar.visibility = View.GONE

                    bindDetailMovie(detailResult.movieDetailModel)
                }
                is MovieDetailEvent.Error -> {
                    binding.detailProgressBar.visibility = View.GONE
                    showMessage( detailResult.error)
                }
                is MovieDetailEvent.Credits -> {
                    castItemAdapter.submitList(detailResult.creditModel.cast)
                }
                else -> {}
            }
        }

        viewModel.favouriteEvent.observeEvent(viewLifecycleOwner) { favouriteEvent ->
            when (favouriteEvent) {
                is MovieDetailEvent.SuccessAdded -> {
                    binding.ivBookMark.setImageResource(R.drawable.ic_bookmark_filled_24)
                    if (favouriteEvent.message.isNotBlank()) showMessage(favouriteEvent.message)
                }
                is MovieDetailEvent.SuccessRemoved -> {
                    binding.ivBookMark.setImageResource(R.drawable.ic_bookmark_border_gray)
                    showMessage(favouriteEvent.message)
                }
                is MovieDetailEvent.Error -> {
                    showMessage(favouriteEvent.error)
                }
                else -> {}
            }
        }

        viewModel.favouriteStatusEvent.observeEvent(viewLifecycleOwner) { isFavourite ->
            val iconBookMarkId = getImageResourceId(isFavourite)
            binding.ivBookMark.setImageResource(iconBookMarkId)
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

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    private fun getImageResourceId(isFavourite: Boolean): Int {
        return if (isFavourite) R.drawable.ic_bookmark_filled_24
        else R.drawable.ic_bookmark_border_gray
    }

    private fun bindDetailMovie(detailDataModel: MovieDetailModel) {

        //use this code in extension function
        /* Glide.with(requireContext())
             .load(IMAGE_URL + detailDataModel.backdrop_path)
             .transition(DrawableTransitionOptions.withCrossFade())
             .into(binding.ivDetail)*/

        viewModel.checkFavouriteMovie(detailDataModel.id)

        with(binding) {
            ivDetail.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
            tvDetailName.text = detailDataModel.original_title
            tvLength.text = detailDataModel.runtime.toHourMinute()
            tvRating.text = (detailDataModel.vote_average / 2).toString()
            tvDescription.text = detailDataModel.overview

            tvDetailStarRate.text =
                root.context.getString(
                    R.string.vote_average_format,
                    detailDataModel.vote_average.toString()
                )

            tvLanguage.text =
                when (detailDataModel.original_language) {
                    "en" -> "English"
                    "ko" -> "Korea"
                    "ja" -> "Japan"
                    "fr" -> "France"
                    "ch" -> "China"
                    else -> detailDataModel.original_language
                }

            genreItemAdapter.submitList(detailDataModel.genres)

            ivBookMark.setOnClickListener {
                viewModel.toggleFavourite(detailDataModel)
            }
        }
    }
}