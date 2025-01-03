package com.pab.nutritrack.ui.rasio

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pab.nutritrack.R
import com.pab.nutritrack.adapter.PickRecipeAdapter
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.databinding.FragmentPickFoodDialogBinding
import com.pab.nutritrack.ui.fragment.recipe.RecipeViewModel
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class PickFoodDialogFragment(val progressRasioActivity: ProgressRasioActivity) : DialogFragment() {

    private var _binding: FragmentPickFoodDialogBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val recipeViewModel: RecipeViewModel by viewModels { viewModel }
    private lateinit var pickRecipeAdapter: PickRecipeAdapter

    companion object {
        fun newInstance(progressRasioActivity: ProgressRasioActivity): PickFoodDialogFragment {
            return PickFoodDialogFragment(progressRasioActivity)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPickFoodDialogBinding.inflate(inflater, container, false)
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

    fun addProgress(idFood: String) {
        dismiss()
        progressRasioActivity.addProgress(idFood)
    }

    private fun setRecipe(it: List<RecipeItem>) {
        pickRecipeAdapter = PickRecipeAdapter(it, this)
        binding.rvRecipe.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pickRecipeAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog = getDialog()!!
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}