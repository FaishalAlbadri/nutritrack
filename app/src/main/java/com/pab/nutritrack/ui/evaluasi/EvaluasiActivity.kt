package com.pab.nutritrack.ui.evaluasi

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivityEvaluasiBinding
import kotlin.random.Random

class EvaluasiActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityEvaluasiBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityEvaluasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val randomNumber = Random.nextInt(75, 91)

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            txtSkorVal.text = "$randomNumber"

            cpSkor.apply {
                progressMax = 100f
                setProgressWithAnimation(randomNumber.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 7f
                progressBarColor = ContextCompat.getColor(this@EvaluasiActivity, R.color.green)
            }
        }
    }
}