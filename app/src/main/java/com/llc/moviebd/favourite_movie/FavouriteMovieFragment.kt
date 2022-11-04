package com.llc.moviebd.favourite_movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.database.FavouriteMovieEntity
import com.llc.moviebd.database.MovieRoomDatabase
import com.llc.moviebd.databinding.FragmentFavouriteMovieBinding

class FavouriteMovieFragment : Fragment() {

    private val viewModel: FavouriteViewModel by viewModels()

    private var _binding: FragmentFavouriteMovieBinding? = null
    private val binding get() = _binding!!

    private val appDatabase by lazy {
        MovieRoomDatabase.getDatabase(requireContext())
    }

    private val favouriteItemAdapter: FavouriteItemAdapter by lazy {
        FavouriteItemAdapter { movieEntity ->
            goToDetails(movieEntity)
        }
    }

    private fun goToDetails(favouriteMovieEntity: FavouriteMovieEntity) {
        val action = FavouriteMovieFragmentDirections
            .actionFavouriteMovieFragmentToMovieDetailFragment(favouriteMovieEntity.id.toString())
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

        viewModel.setAppDatabase(appDatabase)
        viewModel.getAllFavourite()
        viewModel.favouriteUEvent.observe(viewLifecycleOwner) { favouriteEvent->
            when (favouriteEvent) {

                is FavouriteEvent.Success -> {
                    favouriteItemAdapter.submitList(favouriteEvent.movieList)
                }
                is FavouriteEvent.Failure -> {
                   showMessage(favouriteEvent.message)
                }
                else -> {}
            }
        }

        binding.rvMoviesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favouriteItemAdapter
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
}