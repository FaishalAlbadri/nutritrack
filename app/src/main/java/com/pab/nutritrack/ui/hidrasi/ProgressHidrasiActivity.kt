package com.pab.nutritrack.ui.hidrasi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivityProgressHidrasiBinding
import com.pab.nutritrack.utils.picker.ScreenUtils
import com.pab.nutritrack.utils.picker.SliderAdapter
import com.pab.nutritrack.utils.picker.SliderLayoutManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProgressHidrasiActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityProgressHidrasiBinding? = null
    val binding get() = _binding!!
    private val data: ArrayList<String> =
        mutableListOf("100 ML", "250 ML", "500 ML", "750 ML", "1 Liter") as ArrayList<String>
    private val dataLiter: ArrayList<Double> =
        mutableListOf(0.1, 0.25, 0.5, 0.75, 1) as ArrayList<Double>

    private var waterVal: Double = 0.0
    private var waterValNow: Double = 0.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityProgressHidrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            txtKaloriVal.text = "${waterVal} L"

            cpHidrasi.apply {
                progressMax = 2f
                setProgressWithAnimation(waterVal.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 7f
                progressBarColor =
                    ContextCompat.getColor(this@ProgressHidrasiActivity, R.color.green)
            }

            rvWater.apply {
                val padding: Int =
                    ScreenUtils.getScreenWidth(this@ProgressHidrasiActivity) / 2 - ScreenUtils.dpToPx(
                        this@ProgressHidrasiActivity,
                        40
                    )
                layoutManager = SliderLayoutManager(this@ProgressHidrasiActivity).apply {
                    callback = object : SliderLayoutManager.OnItemSelectedListener {
                        override fun onItemSelected(layoutPosition: Int) {
                            waterValNow = dataLiter[layoutPosition]
                        }

                    }
                }

                adapter = SliderAdapter().apply {
                    setData(data)
                    callback = object : SliderAdapter.Callback {
                        override fun onItemClicked(view: View) {
                            rvWater.smoothScrollToPosition(rvWater.getChildLayoutPosition(view))
                        }

                    }
                }
            }

            btnAdd.setOnClickListener {
                waterVal += waterValNow
                txtKaloriVal.text = "${waterVal} L"
                cpHidrasi.apply {
                    progressMax = 2f
                    setProgressWithAnimation(waterVal.toFloat(), 1000)
                    progressBarWidth = 5f
                    backgroundProgressBarWidth = 7f
                    progressBarColor =
                        ContextCompat.getColor(this@ProgressHidrasiActivity, R.color.green)
                }
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