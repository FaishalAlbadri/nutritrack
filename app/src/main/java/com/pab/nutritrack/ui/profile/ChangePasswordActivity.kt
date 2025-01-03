package com.pab.nutritrack.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivityChangePasswordBinding
import com.pab.nutritrack.ui.fragment.setting.SettingViewModel
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
    private var _binding: ActivityChangePasswordBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnChange.setOnClickListener {
                if (edtPassword.text.length < 8 || edtNewPassword.text.length < 8 || edtNewPasswordConfirm.text.length < 8) {
                    Toast.makeText(
                        applicationContext,
                        "Data tidak boleh kosong atau kurang dari 8 karakter!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (edtNewPassword.text.toString()
                            .equals(edtNewPasswordConfirm.text.toString())
                    ) {
                        settingViewModel.changePassword(
                            edtPassword.text.toString(),
                            edtNewPassword.text.toString()
                        )
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Konfirmasi Kata Sandi tidak cocok!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        settingViewModel.apply {
            isLoading.observe(this@ChangePasswordActivity) {
                showLoading(it)
            }
            message.observe(this@ChangePasswordActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    if (it.equals("Berhasil")) {
                        onBackPressedCallback.handleOnBackPressed()
                    }
                }
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