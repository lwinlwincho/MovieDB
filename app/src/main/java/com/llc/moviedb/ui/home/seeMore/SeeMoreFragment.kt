package com.llc.moviedb.ui.home.seeMore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.moviebd.R
import com.llc.moviebd.databinding.FragmentSeeMoreBinding
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.ui.home.Category
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.seeMoreUiEvent.observe(viewLifecycleOwner) { seeMoreEvent->
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