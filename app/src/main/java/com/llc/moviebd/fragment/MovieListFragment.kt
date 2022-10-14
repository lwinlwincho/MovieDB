package com.llc.moviebd.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.llc.moviebd.data.*
import com.llc.moviebd.databinding.FragmentMovieListBinding

class MovieListFragment : Fragment(),Delegate {

    private val viewModel:MovieViewModel by viewModels()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(listener = {}, delegate = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMovieListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiEvent.observe(viewLifecycleOwner){ movieList->
            itemAdapter.setMovieList(movieList)
        }

        binding.rvMovieList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = itemAdapter
        }

        binding.rvMovieList.setHasFixedSize(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onClickListener(model: MovieModel) {

        val action=MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(model)
        findNavController().navigate(action)

        Toast.makeText(context,model.toString(),Toast.LENGTH_LONG).show()
        //Toast.makeText(this, model.toString(), Toast.LENGTH_LONG).show()
    }

}