package com.llc.moviebd.ui.home.detail_movie

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
import com.llc.moviebd.data.data_result.MovieDetailEvent
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.extension.toHourMinute
import com.llc.moviebd.network.IMAGE_URL
import com.llc.moviebd.ui.home.cast.CastItemAdapter
import com.llc.moviebd.ui.home.genre.GenreItemAdapter

class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()

    private var _binding: FragmentMovieDetailBinding? = null
    val binding get() = _binding!!

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
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
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
                    binding.detailScrollView.visibility=View.VISIBLE
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

        /* Glide.with(requireContext())
             .load(IMAGE_URL + detailDataModel.backdrop_path)
             .transition(DrawableTransitionOptions.withCrossFade())
             .into(binding.ivDetail)*/

        binding.ivDetail.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
        binding.tvDetailName.text = detailDataModel.original_title
        //binding.tvLength.text = hourMinute(detailDataModel.runtime) Using sample fun
        //using extension function
        binding.tvLength.text = detailDataModel.runtime.toHourMinute()
        binding.tvRating.text = (detailDataModel.vote_average / 2).toString()
        binding.tvDescription.text = detailDataModel.overview

        binding.tvDetailStarRate.text =
            binding.root.context.getString(
                R.string.vote_average_format,
                detailDataModel.vote_average.toString()
            )

        binding.tvLanguage.text =
            if (detailDataModel.original_language == "en") "English"
            else if (detailDataModel.original_language == "ko") "Korea"
            else if (detailDataModel.original_language == "ja") "Japan"
            else if (detailDataModel.original_language == "fr") "France"
            else if (detailDataModel.original_language == "ch") "China"
            else detailDataModel.original_language

        genreItemAdapter.submitList(detailDataModel.genres)
    }
}