package com.pab.nutritrack.ui.signuser

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.data.UserData
import com.pab.nutritrack.databinding.ActivitySignInBinding
import com.pab.nutritrack.ui.MainActivity
import com.pab.nutritrack.ui.intro.IntroductionActivity
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.htmlStringFormat
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySignInBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val signUserViewModel: SignUserViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
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

            btnSignup.apply {
                text = htmlStringFormat(this@SignInActivity, "Anda belum punya akun? ", "Daftar")
                setOnClickListener {
                    startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                    finish()
                }
            }

            btnSignin.setOnClickListener {
                if (edtUsername.length() == 0 || edtPassword.length() < 8) {
                    if (edtUsername.length() == 0) {
                        Toast.makeText(applicationContext, "Nama Pengguna tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    }
                    if (edtPassword.length() < 8) {
                        Toast.makeText(applicationContext, "Kata Sandi tidak boleh kurang dari 8!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    signUserViewModel.signin(edtUsername.text.toString(), edtPassword.text.toString())
                }
            }
        }

        signUserViewModel.apply {
            isLoading.observe(this@SignInActivity) {
                showLoading(it)
            }
            message.observe(this@SignInActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@SignInActivity) {
                var intro = true

                if (it.userHeight == null) {
                    intro = false
                }

                saveSession(UserData(it.idUser, true, intro))

                if (!intro) {
                    startActivity(Intent(this@SignInActivity, IntroductionActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                } else {
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
                finish()
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