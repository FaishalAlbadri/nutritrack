package com.pab.nutritrack.ui.form.result

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.databinding.ActivityQuestionResultBinding
import com.pab.nutritrack.ui.MainActivity
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.parcelable
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class QuestionResultActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityQuestionResultBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val questionResultViewModel: QuestionResultViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    private lateinit var programData: String
    private lateinit var questionData: QuestionData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityQuestionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        questionData = intent.parcelable<QuestionData>("questionData")!!

        createLoading()

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnNext.setOnClickListener {
                if (!programData.isEmpty()) {
                    questionResultViewModel.personalData(questionData, programData, txtResult.text.toString())
                }
            }
        }

        questionResultViewModel.apply {
            openAI(questionData)

            isLoading.observe(this@QuestionResultActivity) {
                showLoading(it)
            }
            message.observe(this@QuestionResultActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
            resultOpenAI.observe(this@QuestionResultActivity) {
                it.getContentIfNotHandled()?.let {
                    programData = it
                    binding.txtResult.text =
                        "Untuk ${questionData.tujuan} kamu, NutriTrack menyarankan kamu untuk setiap hari\n${it}"
                }
            }

            resultProgram.observe(this@QuestionResultActivity) {
                it.getContentIfNotHandled()?.let {
                    saveIntro()
                    startActivity(
                        Intent(this@QuestionResultActivity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
                finish()
            }
        }
    }

    private fun createLoading() {
        loading = createAlertDialog(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }
}