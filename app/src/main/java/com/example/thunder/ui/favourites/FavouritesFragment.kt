package com.example.thunder.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thunder.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private val viewModel: FavouritesViewModel by viewModels()
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favAdapter: FavouritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addToFavFAB.setOnClickListener() {
            viewModel.navigateToFavPlacesMap(it)
        }
        favAdapter = FavouritesAdapter(arrayListOf()) { view, item ->
            viewModel.navigateToPlaceDetails(view, item)
        }

        viewModel.getSavedFavouritePlaces().asLiveData().observe(viewLifecycleOwner) {
            favAdapter.changeData(it)
        }
        binding.favouritesRecyclerView.apply {
            adapter = favAdapter
        }
        binding.favouritesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}