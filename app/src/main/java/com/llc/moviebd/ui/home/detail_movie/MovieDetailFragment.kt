package com.llc.moviebd.ui.home.detail_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.llc.moviebd.data.data_result.MovieDetailEvent
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL
import com.llc.moviebd.ui.home.genre.GenreItemAdapter

class MovieDetailFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by viewModels()

    private var _binding: FragmentMovieDetailBinding? = null
    val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

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
        viewModel.detailUIEvent.observe(viewLifecycleOwner) { detailResult ->
            when (detailResult) {
                is MovieDetailEvent.Loading -> {
                    binding.detailProgressBar.visibility = View.VISIBLE
                }
                is MovieDetailEvent.Success -> {
                    //bindDetailPoster(movieId)
                    bindDetailMovie(detailResult.movieDetailModel)
                    binding.detailProgressBar.visibility = View.GONE
                }
                is MovieDetailEvent.Error -> {
                    binding.detailProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun bindDetailMovie(detailDataModel: MovieDetailModel) {
        //binding.imvDetailCover.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
        //binding.rating.rating = (detailDataModel.vote_average / 2).toFloat()
        //binding.imvAdult.visibility = if (detailDataModel.adult) View.VISIBLE else View.GONE

        binding.ivDetail.loadFromUrl(IMAGE_URL + detailDataModel.poster_path)
        binding.tvDetailName.text = detailDataModel.original_title
        binding.tvDetailStarRate.text = detailDataModel.imdb_id
        binding.tvGenres.text = detailDataModel.genres.first().name
        binding.tvLanguage.text =
            if (detailDataModel.original_language == "en") "English"
            else detailDataModel.original_language
        binding.tvRating.text = (detailDataModel.vote_average / 2).toString()
        binding.tvDescription.text = detailDataModel.overview

    }
}