package com.pab.nutritrack.ui.aktivitas

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivityProgressAktivitasBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProgressAktivitasActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityProgressAktivitasBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityProgressAktivitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            cpHidrasi.apply {
                progressMax = 10000f
                setProgressWithAnimation(3200f, 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 7f
                progressBarColor =
                    ContextCompat.getColor(this@ProgressAktivitasActivity, R.color.primary)
            }

            txtCalender.apply {
                val kal: Calendar = Calendar.getInstance()
                val kalPlus7: Calendar = Calendar.getInstance()
                val formatter = SimpleDateFormat("MMM, dd", Locale("id", "ID"))
                kalPlus7.add(Calendar.DAY_OF_MONTH, 7)
                val formatterPlus7 = SimpleDateFormat("dd", Locale("id", "ID"))

                text = "${formatter.format(kal.time)} - ${formatterPlus7.format(kalPlus7.time)}"
            }
        }
    }
}