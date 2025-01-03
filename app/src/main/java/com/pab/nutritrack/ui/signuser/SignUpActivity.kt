package com.pab.nutritrack.ui.signuser

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivitySignUpBinding
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.htmlStringFormat
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val signUserViewModel: SignUserViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            edtUsername.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        val input = s.toString()
                        if (input.contains(" ")) {
                            setText(input.replace(" ", ""))
                            setSelection(text.length)
                        }
                    }

                })
            }

            btnSignin.apply {
                text = htmlStringFormat(this@SignUpActivity, "Anda sudah punya akun?", "Masuk")
                setOnClickListener {
                    startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                    finish()
                }
            }

            btnSignup.setOnClickListener {
                if (edtUsername.length() == 0 || edtPassword.length() < 8 || edtPasswordConfirm.length() < 8 || edtPasswordConfirm.text.toString() != edtPassword.text.toString()) {
                    if (edtUsername.length() == 0) {
                        Toast.makeText(applicationContext, "Nama Pengguna tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    }
                    if (edtPassword.length() < 8) {
                        Toast.makeText(applicationContext, "Kata Sandi tidak boleh kurang dari 8!", Toast.LENGTH_SHORT).show()
                    }
                    if (edtPasswordConfirm.text.toString() != edtPassword.text.toString()) {
                        Toast.makeText(applicationContext, "Konfirmasi Kata Sandi tidak cocok!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    signUserViewModel.signup(edtUsername.text.toString(), edtPassword.text.toString())
                }
            }
        }

        signUserViewModel.apply {
            isLoading.observe(this@SignUpActivity) {
                showLoading(it)
            }
            message.observe(this@SignUpActivity) {
                it.getContentIfNotHandled()?.let {
                    if (it.equals("Berhasil")) {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                        finish()
                    } else if (it.isNullOrBlank() || it.isNullOrEmpty()) {
                        Toast.makeText(applicationContext, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
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