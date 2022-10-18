package com.llc.moviebd.data.movie_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.llc.moviebd.R
import com.llc.moviebd.data.Delegate
import com.llc.moviebd.data.ItemAdapter
import com.llc.moviebd.data.MovieModel
import com.llc.moviebd.data.movie_poster.MovieListViewModel
import com.llc.moviebd.databinding.FragmentMovieDetailBinding
import com.llc.moviebd.network.OverViewModel


class MovieDetailFragment : Fragment(), Delegate {

    //private val viewModel: MovieListViewModel by viewModels()

    private val viewModel: OverViewModel by viewModels()

    private var _binding: FragmentMovieDetailBinding? = null
    val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()

    private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(listener = {}, delegate = this)
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
        var movieId=args.movieModel

        binding.imvDetail.setImageResource(R.drawable.ic_baseline_broken_image_24)
        binding.imvDetail.setImageResource(R.drawable.ic_baseline_double_arrow_24)
        /*imgView.load(imgUri) {
            placeholder(R.drawable.ic_baseline_double_arrow_24)
            error(R.drawable.ic_baseline_broken_image_24)
        }*/
        viewModel.photo.observe(viewLifecycleOwner){ movieId->
            binding.imvDetail.load(movieId.imgSrcUrl)
        }
        viewModel.status.observe(viewLifecycleOwner) { movieStatus ->
            binding.txtMovieDescription.text = movieStatus
        }
        viewModel.date.observe(viewLifecycleOwner) { movieDate ->
            binding.txtMovieDetail.text = movieDate
        }

       // binding.imvDetail.setImageResource(args.movieModel.image)
       // binding.txtMovieDescription.text = getString(args.movieModel.description)
        //binding.txtMovieDetail.text = getString(args.movieModel.date)

    }

    override fun onClickListener(model: MovieModel) {
        TODO("Not yet implemented")
    }

}
