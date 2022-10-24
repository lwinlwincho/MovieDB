package com.llc.moviebd.ui.home.movielist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.databinding.FragmentNowShowingListBinding
import com.llc.moviebd.network.HomeMovieListViewModel
import com.llc.moviebd.network.MovieUpcomingEvent
import com.llc.moviebd.ui.home.HomeMovieListFragmentDirections
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter

class NowShowingListFragment : Fragment() {

    private val viewModel:HomeMovieListViewModel by viewModels()

   private var _binding: FragmentNowShowingListBinding? = null
    private val binding get() = _binding!!

    private val nowShowingItemAdapter:NowShowingItemAdapter by lazy {
        NowShowingItemAdapter{ movieModel->
            goToDetails(movieModel)
        }
    }

    private fun goToDetails(movieModel: MovieModel) {
        val action = NowShowingListFragmentDirections
            .actionMovieListShowFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNowShowingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNowShowingMoviesList.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = nowShowingItemAdapter
        }

        viewModel.nowShowingUiEvent.observe(viewLifecycleOwner) { nowShowingEvent ->
            when (nowShowingEvent) {
                is MovieUpcomingEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is MovieUpcomingEvent.Success -> {
                    nowShowingItemAdapter.submitList(nowShowingEvent.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is MovieUpcomingEvent.Failure -> {
                    Toast.makeText(requireContext(), nowShowingEvent.message, Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }
}