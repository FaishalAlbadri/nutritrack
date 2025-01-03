package com.pab.nutritrack.ui.form.question

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.databinding.ActivityQuestDailyBinding
import com.pab.nutritrack.utils.parcelable

class QuestDailyActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityQuestDailyBinding? = null
    val binding get() = _binding!!

    private lateinit var questionData: QuestionData
    private var answerOne = ""
    private var answerTwo = ""
    private var answerFour = ""
    private var answerFive = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityQuestDailyBinding.inflate(layoutInflater)
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

            ArrayAdapter.createFromResource(
                this@QuestDailyActivity,
                R.array.olahraga_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerQuest3.adapter = adapter
            }

            rgQuest4.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerFour = selectedRadioButton.text.toString()
            }

            rgQuest5.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                answerFive = selectedRadioButton.text.toString()
            }

            btnNext.setOnClickListener {
                if (answerOne.isEmpty() || answerTwo.isEmpty() || spinnerQuest3.selectedItemPosition == 0 || answerFour.isEmpty() || answerFive.isEmpty()) {
                    Toast.makeText(
                        this@QuestDailyActivity, "Semua data wajib diisi!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    questionData.let {
                        startActivity(
                            Intent(this@QuestDailyActivity, QuestFoodActivity::class.java).putExtra(
                                "questionData", QuestionData(
                                    it.tanggalLahir,
                                    it.jenisKelamin,
                                    it.tinggiBadan,
                                    it.beratBadan,
                                    it.tujuan,
                                    it.waterQuestOne,
                                    it.waterQuestTwo,
                                    it.waterQuestThree,
                                    answerOne,
                                    answerTwo,
                                    spinnerQuest3.selectedItem.toString(),
                                    answerFour,
                                    answerFive,
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