package com.pab.nutritrack.ui.alarm

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pab.nutritrack.R
import com.pab.nutritrack.data.AlarmData
import com.pab.nutritrack.databinding.ActivityAlarmDetailBinding
import com.pab.nutritrack.ui.fragment.alarm.AlarmViewModel
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.parcelable
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class AlarmDetailActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityAlarmDetailBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val alarmViewModel: AlarmViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    private var isAddAlarm = true
    private lateinit var alarmData: AlarmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityAlarmDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        alarmData = intent.parcelable<AlarmData>("data")!!

        if (alarmData.id > 0) {
            isAddAlarm = false
        }

        setView()

        alarmViewModel.apply {
            isLoading.observe(this@AlarmDetailActivity) {
                showLoading(it)
            }

            message.observe(this@AlarmDetailActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
                onBackPressedCallback.handleOnBackPressed()
            }
        }
    }

    private fun setView() = binding.apply {
        createLoading()
        btnBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        txtTitle.text = if (isAddAlarm) "Tambah Pengingat" else "Ubah Pengingat"
        npHour.apply {
            maxValue = 23
            minValue = 0
            value = alarmData.hour
        }

        npMinutes.apply {
            maxValue = 59
            minValue = 0
            value = alarmData.minutes
        }

        if (!isAddAlarm) {
            edtTitle.setText(alarmData.title)
            setBackgorundDay(txtSenin, alarmData.senin)
            setBackgorundDay(txtSelasa, alarmData.selasa)
            setBackgorundDay(txtRabu, alarmData.rabu)
            setBackgorundDay(txtKamis, alarmData.kamis)
            setBackgorundDay(txtJumat, alarmData.jumat)
            setBackgorundDay(txtSabtu, alarmData.sabtu)
            setBackgorundDay(txtMinggu, alarmData.minggu)
        }

        txtSenin.setOnClickListener {
            changeBackgroundDay(txtSenin)
        }

        txtSelasa.setOnClickListener {
            changeBackgroundDay(txtSelasa)
        }

        txtRabu.setOnClickListener {
            changeBackgroundDay(txtRabu)
        }

        txtKamis.setOnClickListener {
            changeBackgroundDay(txtKamis)
        }

        txtJumat.setOnClickListener {
            changeBackgroundDay(txtJumat)
        }

        txtSabtu.setOnClickListener {
            changeBackgroundDay(txtSabtu)
        }

        txtMinggu.setOnClickListener {
            changeBackgroundDay(txtMinggu)
        }

        btnDelete.setOnClickListener {
            if (isAddAlarm) {
                onBackPressedCallback.handleOnBackPressed()
            } else {
                alarmViewModel.deleteAlarm(alarmData.id)
            }
        }

        btnSave.setOnClickListener {
            if (edtTitle.text.isNotEmpty()) {
                val senin = if (txtSenin.tag == "active") true else false
                val selasa = if (txtSelasa.tag == "active") true else false
                val rabu = if (txtRabu.tag == "active") true else false
                val kamis = if (txtKamis.tag == "active") true else false
                val jumat = if (txtJumat.tag == "active") true else false
                val sabtu = if (txtSabtu.tag == "active") true else false
                val minggu = if (txtMinggu.tag == "active") true else false
                val dataHour = npHour.value
                val dataMinutes = npMinutes.value

                if (isAddAlarm) {
                    alarmViewModel.addAlarm(AlarmData(0, edtTitle.text.toString(), dataHour, dataMinutes, senin, selasa, rabu, kamis, jumat, sabtu, minggu, true))
                } else {
                    alarmViewModel.addAlarm(AlarmData(alarmData.id, edtTitle.text.toString(), dataHour, dataMinutes, senin, selasa, rabu, kamis, jumat, sabtu, minggu, true))
                }
            } else {
                Toast.makeText(applicationContext, "Nama Pengingat tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBackgorundDay(textView: TextView, boolean: Boolean) {
        val bgDefault = ContextCompat.getDrawable(this, R.drawable.bg_rounded_day_default)
        val bgActive = ContextCompat.getDrawable(this, R.drawable.bg_rounded_day_active)
        textView.background = if (boolean) bgActive else bgDefault
        textView.setTextColor(if (boolean) Color.WHITE else Color.BLACK)
        textView.tag = if (boolean) "active" else "default"
    }

    private fun changeBackgroundDay(textView: TextView) {
        val bgDefault = ContextCompat.getDrawable(this, R.drawable.bg_rounded_day_default)
        val bgActive = ContextCompat.getDrawable(this, R.drawable.bg_rounded_day_active)
        textView.background = if (textView.tag == "active") bgDefault else bgActive
        textView.setTextColor(if (textView.tag == "active") Color.BLACK else Color.WHITE)
        textView.tag = if (textView.tag == "active") "default" else "active"
    }

    private fun createLoading() {
        loading = createAlertDialog(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }
}