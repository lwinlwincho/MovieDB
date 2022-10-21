package com.llc.moviebd.data.detail_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.llc.moviebd.data.data_result.MovieDetailEvent
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.data.poster_movie.Delegate
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

class MovieDetailFragment : Fragment(), Delegate {

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
        // binding.imvDetail.loadFromUrl(detailDataModel.id.toString())

        binding.imvDetailCover.loadFromUrl(IMAGE_URL + detailDataModel.backdrop_path)
        binding.imvDetail.loadFromUrl(IMAGE_URL + detailDataModel.poster_path)
        binding.rating.rating = (detailDataModel.vote_average / 2).toFloat()

        binding.imvAdult.visibility = if (detailDataModel.adult) View.VISIBLE else View.GONE

        binding.txtGenres.text = detailDataModel.genres[1].name

        binding.txtHomePage.text = detailDataModel.homepage

        binding.txtOriginalLanguage.text =
            if (detailDataModel.original_language == "en") "English"
            else detailDataModel.original_language

        binding.txtOriginalTitle.text = detailDataModel.original_title
        binding.txtOverview.text = detailDataModel.overview
        binding.txtPopularity.text = detailDataModel.popularity.toString()
        binding.txtProductionCompanies.text = detailDataModel.production_companies[0].name
        //   binding.txtProductionCountries.text = detailDataModel.production_countries[1].name
        binding.txtReleaseDate.text = detailDataModel.release_date
        binding.txtRevenue.text = detailDataModel.revenue.toString()
        //binding.txtSpokenLanguages.text = detailDataModel.spoken_languages[1].toString()
    }

    override fun onClicklistener(movieModel: MovieModel) {
    }
}