package com.pab.nutritrack.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.R
import com.pab.nutritrack.databinding.ActivityIntroductionBinding
import com.pab.nutritrack.ui.form.data.PersonalDataActivity

class IntroductionActivity : AppCompatActivity() {

    private var _binding: ActivityIntroductionBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnNext.setOnClickListener {
                startActivity(Intent(this@IntroductionActivity, PersonalDataActivity::class.java))
            }
        }
    }
}