package com.pab.nutritrack.ui.form.data

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.data.QuestionData
import com.pab.nutritrack.databinding.ActivityPersonalDataBinding
import com.pab.nutritrack.ui.form.question.QuestWaterActivity
import java.util.Calendar

class PersonalDataActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityPersonalDataBinding? = null
    val binding get() = _binding!!

    private var genderAnswer = ""
    private var goalsAnswer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnBirthday.setOnClickListener {
                val kal: Calendar = Calendar.getInstance()
                DatePickerDialog(
                    this@PersonalDataActivity,
                    this@PersonalDataActivity,
                    kal.get(Calendar.YEAR),
                    kal.get(Calendar.MONTH),
                    kal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            rgGender.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                genderAnswer = selectedRadioButton.text.toString()
            }

            rgGoals.setOnCheckedChangeListener { group, checkedId ->
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                goalsAnswer = selectedRadioButton.text.toString()
            }

            btnNext.setOnClickListener {

                if (edtBirthday.text.toString().isEmpty() || edtHeight.text.toString()
                        .isEmpty() || edtWeight.text.toString()
                        .isEmpty() || genderAnswer.isEmpty() || goalsAnswer.isEmpty()
                ) {
                    Toast.makeText(
                        this@PersonalDataActivity, "Semua data wajib diisi!", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    startActivity(
                        Intent(this@PersonalDataActivity, QuestWaterActivity::class.java).putExtra(
                                "questionData", QuestionData(
                                    edtBirthday.text.toString(),
                                    genderAnswer,
                                    edtHeight.text.toString(),
                                    edtWeight.text.toString(),
                                    goalsAnswer,
                                    "",
                                    "",
                                    "",
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.apply {
            edtBirthday.setText("${year}-${month + 1}-${dayOfMonth}")
        }
    }
}