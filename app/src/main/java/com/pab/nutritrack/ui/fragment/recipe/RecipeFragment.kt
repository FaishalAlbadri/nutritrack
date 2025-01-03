package com.pab.nutritrack.ui.fragment.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pab.nutritrack.R
import com.pab.nutritrack.adapter.RecipeAdapter
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.databinding.FragmentRecipeBinding
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val recipeViewModel: RecipeViewModel by viewModels { viewModel }
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        recipeViewModel.apply {
            recipeResponse.observe(viewLifecycleOwner) {
                setRecipe(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            getRecipe()
        }
    }

    private fun setRecipe(it: List<RecipeItem>) {
        recipeAdapter = RecipeAdapter(it)
        binding.rvRecipe.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}