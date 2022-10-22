package com.llc.moviebd.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentMovieListBinding
import com.llc.moviebd.network.*
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter
import com.llc.moviebd.ui.home.popular.PopularItemAdapter

class HomeMovieListFragment : Fragment() {

    private val viewModel: MovieUpcomingViewModel by viewModels()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val nowShowingItemAdapter: NowShowingItemAdapter by lazy {
        NowShowingItemAdapter { movieModel ->
            goToDetails(movieModel)
        }
    }

    private val popularItemAdapter: PopularItemAdapter by lazy {
        PopularItemAdapter { movieModel ->
            goToDetails(movieModel)
        }
    }

    private fun goToDetails(movieModel: MovieModel) {
        val action = HomeMovieListFragmentDirections
            .actionMovieListFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNowShowingMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nowShowingItemAdapter
        }

        binding.rvPopularMovies.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = popularItemAdapter
        }

        viewModel.movieUiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MovieUpcomingEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieUpcomingEvent.Success -> {
                    nowShowingItemAdapter.submitList(event.movieList)
                    popularItemAdapter.submitList(event.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.Failure -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}