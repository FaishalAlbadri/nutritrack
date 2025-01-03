package com.pab.nutritrack.ui.landingpage

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.databinding.ActivityLandingPageBinding
import com.pab.nutritrack.ui.MainActivity
import com.pab.nutritrack.ui.form.data.PersonalDataActivity
import com.pab.nutritrack.ui.intro.IntroductionActivity
import com.pab.nutritrack.ui.signuser.SignInActivity
import com.pab.nutritrack.ui.signuser.SignUpActivity
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory

class LandingPageActivity : AppCompatActivity() {

    private var _binding: ActivityLandingPageBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val landingPageViewModel: LandingPageViewModel by viewModels { viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        landingPageViewModel.getSessionUser().observe(this@LandingPageActivity) {
            if (it.isLogin) {
                it.apply {
                    if (!intro) {
                        startActivity(Intent(this@LandingPageActivity, IntroductionActivity::class.java))
                    } else {
                        startActivity(Intent(this@LandingPageActivity, MainActivity::class.java))
                    }
                    finish()
                }
            }
        }

        binding.apply {
            btnSignin.setOnClickListener {
                startActivity(Intent(this@LandingPageActivity, SignInActivity::class.java))
            }
            btnSignup.setOnClickListener {
                startActivity(Intent(this@LandingPageActivity, SignUpActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}