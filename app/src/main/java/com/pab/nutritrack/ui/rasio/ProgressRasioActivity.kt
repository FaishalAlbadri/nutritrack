package com.pab.nutritrack.ui.rasio

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pab.nutritrack.R
import com.pab.nutritrack.adapter.RecipeAdapter
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriItem
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.databinding.ActivityProgressRasioBinding
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory
import java.util.Calendar

class ProgressRasioActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityProgressRasioBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val progressRasioViewModel: ProgressRasioViewModel by viewModels { viewModel }
    private lateinit var curentTimeNow: String
    private lateinit var idProgressKalori: String
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityProgressRasioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)
        fragmentManager = supportFragmentManager

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnCalender.setOnClickListener {
                val kal: Calendar = Calendar.getInstance()
                DatePickerDialog(
                    this@ProgressRasioActivity,
                    this@ProgressRasioActivity,
                    kal.get(Calendar.YEAR),
                    kal.get(Calendar.MONTH),
                    kal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            btnAdd.setOnClickListener {
                PickFoodDialogFragment.newInstance(this@ProgressRasioActivity)
                    .show(fragmentManager, "")
            }
        }

        progressRasioViewModel.apply {
            progressKaloriResponse.observe(this@ProgressRasioActivity) {
                setHistory(mutableListOf())
                idProgressKalori = it.id
                getHistory(it.id)
                setProgress(it)
            }

            addProgressKalori.observe(this@ProgressRasioActivity) {
                getProgressNow()
            }

            recipeResponse.observe(this@ProgressRasioActivity) {
                setHistory(it)
            }

            message.observe(this@ProgressRasioActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        getProgressNow()
    }

    fun addProgress(idFood: String) {
        progressRasioViewModel.addProgress(idFood, idProgressKalori)
    }

    private fun getProgressNow() {
        val kal: Calendar = Calendar.getInstance()
        curentTimeNow = "${kal.get(Calendar.YEAR)}-${kal.get(Calendar.MONTH) + 1}-${
            kal.get(
                Calendar.DAY_OF_MONTH
            )
        }"
        progressRasioViewModel.getProgress(curentTimeNow)
    }


    private fun setProgress(data: ProgressKaloriItem) {
        binding.apply {
            txtKaloriVal.text = data.progress
            txtKaloriTarget.text = "dari ${data.target} kkal"
            txtProteinVal.text = "${data.progressProtein} / ${data.targetProtein} gram"
            txtKarboVal.text = "${data.progressKarbo} / ${data.targetKarbo} gram"
            txtLemakVal.text = "${data.progressLemak} / ${data.targetLemak} gram"

            cpKalori.apply {
                progressMax = data.target.toFloat()
                setProgressWithAnimation(data.progress.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 7f
                progressBarColor =
                    ContextCompat.getColor(this@ProgressRasioActivity, R.color.primary)
            }

            pbProtein.apply {
                max = data.targetProtein.toInt()
                progress = data.progressProtein.toInt()
            }

            pbKarbo.apply {
                max = data.targetKarbo.toInt()
                progress = data.progressKarbo.toInt()
            }

            pbLemak.apply {
                max = data.targetLemak.toInt()
                progress = data.progressLemak.toInt()
            }
        }
    }

    private fun setHistory(it: List<RecipeItem>) {
        recipeAdapter = RecipeAdapter(it)
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@ProgressRasioActivity)
            adapter = recipeAdapter
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        curentTimeNow = "${year}-${month + 1}-${dayOfMonth}"
        progressRasioViewModel.getProgress(
            curentTimeNow
        )
    }
}