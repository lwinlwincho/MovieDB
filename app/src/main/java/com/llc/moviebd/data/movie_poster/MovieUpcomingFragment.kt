package com.llc.moviebd.data.movie_poster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentMovieListBinding
import com.llc.moviebd.network.*

class MovieUpcomingFragment : Fragment(), Delegate {

    private val viewModel: MovieUpcomingViewModel by viewModels()

    private var _binding: FragmentMovieListBinding? = null
    val binding get() = _binding!!

    private val movieItemAdapter: MovieItemAdapter by lazy {
        MovieItemAdapter(this)
    }

    //YOu can use either "delegate=this" or interface can be directly use "delegate = object:Delegate{...}"
    /*private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(listener = {}, delegate = object :Delegate{
            override fun onClickListener(model: MovieModel) {
                val action=MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(model)
                findNavController().navigate(action)
            }
        }
        )
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* viewModel.uievent.observe(viewLifecycleOwner){ movieList->
             itemAdapter.setMovieList(movieList)
         }
 */
        binding.rvMovieList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieItemAdapter
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MovieEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieEvent.Success -> {
                    movieItemAdapter.submitList(event.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is MovieEvent.Failure -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClicklistener(movieModel: MovieModel) {
        val action =
            MovieUpcomingFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)

    }
}