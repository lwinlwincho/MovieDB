package com.llc.moviedb.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.databinding.FragmentHomeMovieListBinding
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.ui.home.now_showing.NowShowingItemAdapter
import com.llc.moviedb.ui.home.popular.PopularItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeMovieListFragment : Fragment() {

    private val viewModel: HomeMovieListViewModel by viewModels()

    private var _binding: FragmentHomeMovieListBinding? = null
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
        _binding = FragmentHomeMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* viewModel.nowShowingUiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MovieUpcomingEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieUpcomingEvent.Success -> {
                    binding.scrollView.visibility = View.VISIBLE
                    nowShowingItemAdapter.submitList(event.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.Failure -> {
                    showMessage(event.message)
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }

        viewModel.popularUiEvent.observe(viewLifecycleOwner) { popularEvent ->
            when (popularEvent) {
                is MovieUpcomingEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieUpcomingEvent.Success -> {
                    popularItemAdapter.submitList(popularEvent.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.Failure -> {
                    showMessage(popularEvent.message)
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }

        */

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.nowShowingUiEvent.collectLatest { nowShowingUiState ->
                    when (nowShowingUiState) {
                        is MovieUpcomingEvent.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MovieUpcomingEvent.Success -> {
                            nowShowingItemAdapter.submitList(
                                nowShowingUiState.movieList
                            )
                            Toast.makeText(requireContext(),"Now showing", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                        is MovieUpcomingEvent.Failure -> {
                            showMessage(nowShowingUiState.message)
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.popularUiEvent.collectLatest { popularUiState ->
                    when (popularUiState) {
                        is MovieUpcomingEvent.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MovieUpcomingEvent.Success -> {
                            popularItemAdapter.submitList(
                                popularUiState.movieList
                            )
                            binding.progressBar.visibility = View.GONE
                        }
                        is MovieUpcomingEvent.Failure -> {
                            showMessage(popularUiState.message)
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.tvNowShowingSeemore.setOnClickListener {
            val action =
                HomeMovieListFragmentDirections.actionMovieListFragmentToSeeMoreFragment(Category.NOW_SHOWING)
            findNavController().navigate(action)
        }

        binding.tvPopularSeemore.setOnClickListener {
            val action =
                HomeMovieListFragmentDirections.actionMovieListFragmentToSeeMoreFragment(Category.POPULAR)
            findNavController().navigate(action)
        }
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

    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}