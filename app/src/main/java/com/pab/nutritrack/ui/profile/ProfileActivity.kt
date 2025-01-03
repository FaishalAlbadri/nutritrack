package com.pab.nutritrack.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.data.user.UserItem
import com.pab.nutritrack.databinding.ActivityProfileBinding
import com.pab.nutritrack.ui.fragment.setting.SettingViewModel
import com.pab.nutritrack.utils.createAlertDialog
import com.pab.nutritrack.utils.parcelable
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
    private var _binding: ActivityProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog
    private lateinit var userItem: UserItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        userItem = intent.parcelable<UserItem>("data")!!

        createLoading()

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnSave.setOnClickListener {
                settingViewModel.changeUsername(edtUsername.text.toString())
            }

            edtUsername.setText(userItem.userName)
            edtBirthday.setText(userItem.userBirthday)
            edtGender.setText(userItem.userGender)
        }

        settingViewModel.apply {
            isLoading.observe(this@ProfileActivity) {
                showLoading(it)
            }
            message.observe(this@ProfileActivity) {
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