package com.pab.nutritrack.ui.target

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pab.nutritrack.databinding.ActivityTargetBinding
import com.pab.nutritrack.ui.form.data.PersonalDataActivity

class TargetActivity : AppCompatActivity() {

    private var _binding: ActivityTargetBinding? = null
    val binding get() = _binding!!
    private var goals = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTargetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goals = intent.getStringExtra("goals").toString()

        binding.apply {
            txtResult.text = goals
            btnReset.setOnClickListener {
                startActivity(Intent(applicationContext, PersonalDataActivity::class.java))
            }
        }
    }
}