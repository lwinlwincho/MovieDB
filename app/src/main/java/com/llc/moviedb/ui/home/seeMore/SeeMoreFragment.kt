package com.llc.moviedb.ui.home.seeMore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.R
import com.llc.moviebd.databinding.FragmentSeeMoreBinding
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.ui.model.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeeMoreFragment : Fragment() {

    private val viewModel: SeeMoreViewModel by viewModels()

    private var _binding: FragmentSeeMoreBinding? = null
    private val binding get() = _binding!!

    private val args: SeeMoreFragmentArgs by navArgs()

    private val seeMoreItemAdapter: SeeMoreItemAdapter by lazy {
       SeeMoreItemAdapter { movieModel ->
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

/*        viewModel.seeMoreUiEvent.observe(viewLifecycleOwner) { seeMoreEvent->
            when (seeMoreEvent) {
                is SeeMoreEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is SeeMoreEvent.Success -> {
                    seeMoreItemAdapter.submitList(seeMoreEvent.movieList)
                    binding.progressBar.visibility = View.GONE
                }
                is SeeMoreEvent.Failure -> {
                    showMessage(seeMoreEvent.message)
                    binding.progressBar.visibility = View.GONE
                }
                else -> {}
            }
        }*/

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.seeMoreUiEvent.collectLatest { nowShowingUiState ->
                    when (nowShowingUiState) {
                        is SeeMoreEvent.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is SeeMoreEvent.Success -> {
                            seeMoreItemAdapter.submitList(
                                nowShowingUiState.movieList
                            )
                            binding.progressBar.visibility = View.GONE
                        }
                        is SeeMoreEvent.Failure -> {
                            showMessage(nowShowingUiState.message)
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.rvMoviesList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = seeMoreItemAdapter
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