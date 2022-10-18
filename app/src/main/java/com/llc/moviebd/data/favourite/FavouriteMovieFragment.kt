package com.llc.moviebd.data.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.llc.moviebd.data.MovieModel
import com.llc.moviebd.data.movie_poster.MovieListFragmentDirections
import com.llc.moviebd.databinding.FragmentFavouriteMovieBinding

class FavouriteMovieFragment : Fragment(),FavouriteClickListener {

    private val favouriteMovieViewModel:FavouriteMovieViewModel by viewModels()

    private var _binding:FragmentFavouriteMovieBinding?=null
    val binding get()=_binding!!

    private val favArgs:FavouriteMovieFragmentArgs by navArgs()

    private val favItemAdapter:FavouriteMovieAdapter by lazy {
        FavouriteMovieAdapter(favouriteClickListener = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteMovieBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavouriteList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favItemAdapter
        }

        favouriteMovieViewModel.favEvent.observe(viewLifecycleOwner){ favMovieList->
            favItemAdapter.setFavouriteList(favMovieList)
            //favItemAdapter.submitList(favMovieList)
        }

        binding.rvFavouriteList.setHasFixedSize(true)

        /*binding.imvFavourite.setImageResource(favArgs.favouriteMovieModel.image)
        binding.txtMovieFavourite.text=getString(favArgs.favouriteMovieModel.title)
        binding.txtMovieFavDate.text=getString(favArgs.favouriteMovieModel.date)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onFavouritClicked(favouriteModel: MovieModel) {
        val action= MovieListFragmentDirections.actionMovieListFragmentToFavouriteMovieFragment(favouriteModel)
        findNavController().navigate(action)
    }


}