package com.pab.nutritrack.ui.form.question

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.databinding.ActivityQuestWaterBinding
import com.pab.nutritrack.utils.parcelable

class QuestWaterActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityQuestWaterBinding? = null
    val binding get() = _binding!!

    private lateinit var questionData: QuestionData
    private var answerOne = ""
    private var answerTwo = ""
    private var answerThree = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityQuestWaterBinding.inflate(layoutInflater)
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

            btnNext.setOnClickListener {
                if (answerOne.isEmpty() || answerTwo.isEmpty() || answerThree.isEmpty()) {
                    Toast.makeText(
                        this@QuestWaterActivity, "Semua data wajib diisi!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    questionData.let {
                        startActivity(
                            Intent(
                                this@QuestWaterActivity, QuestDailyActivity::class.java
                            ).putExtra(
                                "questionData", QuestionData(
                                    it.tanggalLahir,
                                    it.jenisKelamin,
                                    it.tinggiBadan,
                                    it.beratBadan,
                                    it.tujuan,
                                    answerOne,
                                    answerTwo,
                                    answerThree,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""
                                )
                            )
                        )
                    }
                }
            }

        }
    }
}