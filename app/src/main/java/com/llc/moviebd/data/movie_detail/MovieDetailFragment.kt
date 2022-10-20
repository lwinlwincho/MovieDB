package com.llc.moviebd.data.movie_detail

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
import com.llc.moviebd.data.movie_poster.Delegate
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
        // binding.txtMovieDescription.text = getString(args.movieModel.description)
        //binding.txtMovieDetail.text = getString(args.movieModel.date)
    }

    /* private fun bindDetailPoster(movieId: String) {

         binding.imvDetail.setImageResource(movieId.toInt())
     }*/

    private fun bindDetailMovie(detailDataModel: MovieDetailModel) {
        // binding.imvDetail.loadFromUrl(detailDataModel.id.toString())
        binding.imvDetail.loadFromUrl(IMAGE_URL + detailDataModel.poster_path)
        binding.txtDetailDescription.text = detailDataModel.overview
        binding.txtDetailContent.text = detailDataModel.release_date

    }

    override fun onClicklistener(movieModel: MovieModel) {
        TODO("Not yet implemented")
    }

}
