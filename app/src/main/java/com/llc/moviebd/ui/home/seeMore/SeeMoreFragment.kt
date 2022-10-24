package com.llc.moviebd.ui.home.seeMore

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
import com.llc.moviebd.databinding.FragmentSeeMoreBinding
import com.llc.moviebd.ui.home.Category
import com.llc.moviebd.ui.home.now_showing.NowShowingItemAdapter

class SeeMoreFragment : Fragment() {

    private val viewModel: SeeMoreViewModel by viewModels()

    private var _binding: FragmentSeeMoreBinding? = null
    private val binding get() = _binding!!

    private val args: SeeMoreFragmentArgs by navArgs()

    private val nowShowingItemAdapter: NowShowingItemAdapter by lazy {
        NowShowingItemAdapter { movieModel ->
            goToDetails(movieModel)
        }
    }

    private fun goToDetails(movieModel: MovieModel) {
        val action = SeeMoreFragmentDirections
            .actionMovieListShowFragmentToMovieDetailFragment(movieModel.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.category) {
            Category.NOW_SHOWING -> {
                binding.tvTitle.text = getString(R.string.label_now_showing)
                viewModel.getNowShowing()
            }
            Category.POPULAR -> {
                binding.tvTitle.text = getString(R.string.label_popular)
                viewModel.getPopular()
            }
        }

        binding.rvMoviesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = nowShowingItemAdapter
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.seeMoreUiEvent.observe(viewLifecycleOwner) { nowShowingEvent ->
            when (nowShowingEvent) {
                is SeeMoreEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is SeeMoreEvent.Success -> {
                    nowShowingItemAdapter.submitList(nowShowingEvent.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is SeeMoreEvent.Failure -> {
                    Toast.makeText(requireContext(), nowShowingEvent.message, Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }
    }
}