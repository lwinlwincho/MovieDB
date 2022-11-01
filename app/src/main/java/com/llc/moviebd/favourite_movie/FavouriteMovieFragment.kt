package com.llc.moviebd.favourite_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentFavouriteMovieBinding
import com.llc.moviebd.databinding.FragmentSeeMoreBinding
import com.llc.moviebd.ui.home.Category
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter
import com.llc.moviebd.ui.home.seeMore.SeeMoreEvent
import com.llc.moviebd.ui.home.seeMore.SeeMoreFragmentArgs
import com.llc.moviebd.ui.home.seeMore.SeeMoreFragmentDirections
import com.llc.moviebd.ui.home.seeMore.SeeMoreViewModel

class FavouriteMovieFragment : Fragment() {

    private val viewModel: FavouriteViewModel by viewModels()

    private var _binding: FragmentFavouriteMovieBinding? = null
    private val binding get() = _binding!!

    private val args: SeeMoreFragmentArgs by navArgs()

   /* private val nowShowingItemAdapter: NowShowingItemAdapter by lazy {
        NowShowingItemAdapter { movieModel ->
            goToDetails(movieModel)
        }
    }*/

    private fun goToDetails(movieModel: MovieModel) {
        val action = SeeMoreFragmentDirections
            .actionMovieListShowFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMoviesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
           // adapter = nowShowingItemAdapter
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.favouriteUEvent.observe(viewLifecycleOwner) { favouriteEvent->
            when (favouriteEvent) {
                is FavouriteEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is FavouriteEvent.Success -> {
                  //  nowShowingItemAdapter.submitList(favouriteEvent.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is FavouriteEvent.Failure -> {
                    Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }
}