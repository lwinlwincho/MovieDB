package com.llc.moviebd.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.moviebd.R
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentHomeMovieListBinding
import com.llc.moviebd.singleEvent.observeEvent
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter
import com.llc.moviebd.ui.home.popular.PopularItemAdapter
import com.llc.moviebd.ui.home.popular.onItemClickListener
import com.llc.myinventory.database.MovieRoomDatabase

class HomeMovieListFragment : Fragment(), onItemClickListener {

    private val viewModel: HomeMovieListViewModel by viewModels()

    private var _binding: FragmentHomeMovieListBinding? = null
    private val binding get() = _binding!!

    private val appDatabase by lazy {
        MovieRoomDatabase.getDatabase(requireContext())
    }

    private val nowShowingItemAdapter: NowShowingItemAdapter by lazy {
        NowShowingItemAdapter(this)
    }

    private val popularItemAdapter: PopularItemAdapter by lazy {
        PopularItemAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMovieListBinding.inflate(inflater, container, false)
        return binding.root

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.imvNoti.setOnClickListener {
            val action =
                HomeMovieListFragmentDirections.actionHomeMovieListFragmentToFavouriteMovieFragment()
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

        viewModel.nowShowingUiEvent.observe(viewLifecycleOwner) { event ->
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
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
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
                    Toast.makeText(requireContext(), popularEvent.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }

        viewModel.favouriteUiEvent.observeEvent(viewLifecycleOwner) { favouriteEvent ->
            when (favouriteEvent) {
                is MovieUpcomingEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieUpcomingEvent.SuccessAddedSms -> {
                    if (favouriteEvent.message.isNotBlank()) {
                        Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.SuccessRemovedSms -> {
                    // popularItemAdapter.submitList(popularEvent.movieList)
                    Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.Failure -> {
                    Toast.makeText(requireContext(), favouriteEvent.message, Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }

    override fun onPosterClicked(model: MovieModel) {
        goToDetails(model)
    }

    override fun onFavoriteClicked(model: MovieModel) {
        addFav(model)
    }

    private fun goToDetails(movieModel: MovieModel) {
        val action = HomeMovieListFragmentDirections
            .actionMovieListFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)
    }

    private fun addFav(model: MovieModel) {
        viewModel.addFavourite(
            appDatabase = appDatabase,
            posterPath = model.posterPath.toString(),
            title = model.title,
            releaseDate = model.releaseDate,
            voteAverage = model.vote_average.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}