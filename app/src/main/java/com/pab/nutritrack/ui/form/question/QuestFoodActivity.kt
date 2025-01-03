package com.pab.nutritrack.ui.form.question

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pab.nutritrack.R
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.databinding.ActivityQuestFoodBinding
import com.pab.nutritrack.ui.MainActivity
import com.pab.nutritrack.ui.form.result.QuestionResultActivity
import com.pab.nutritrack.utils.parcelable

class QuestFoodActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityQuestFoodBinding? = null
    val binding get() = _binding!!

    private lateinit var questionData: QuestionData
    private var answerOne = ""
    private var answerTwo = ""
    private var answerThree = ""
    private var answerFour = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityQuestFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionData = intent.parcelable<QuestionData>("questionData")!!

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            rgQuest1.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerOne = selectedRadioButton.text.toString()
            }

            rgQuest2.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerTwo = selectedRadioButton.text.toString()
            }

            rgQuest3.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerThree = selectedRadioButton.text.toString()
            }

            rgQuest4.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerFour = selectedRadioButton.text.toString()
            }

            btnFinish.setOnClickListener {
                if (answerOne.isEmpty() || answerTwo.isEmpty() || answerThree.isEmpty() || answerFour.isEmpty()) {
                    Toast.makeText(
                        this@QuestFoodActivity, "Semua data wajib diisi!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    questionData.let {
                        startActivity(
                            Intent(
                                this@QuestFoodActivity,
                                QuestionResultActivity::class.java
                            ).putExtra(
                                "questionData", QuestionData(
                                    it.tanggalLahir,
                                    it.jenisKelamin,
                                    it.tinggiBadan,
                                    it.beratBadan,
                                    it.tujuan,
                                    it.waterQuestOne,
                                    it.waterQuestTwo,
                                    it.waterQuestThree,
                                    it.dailyQuestOne,
                                    it.dailyQuestTwo,
                                    it.dailyQuestThree,
                                    it.dailyQuestFour,
                                    it.dailyQuestFive,
                                    answerOne,
                                    answerTwo,
                                    answerThree,
                                    answerFour
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}