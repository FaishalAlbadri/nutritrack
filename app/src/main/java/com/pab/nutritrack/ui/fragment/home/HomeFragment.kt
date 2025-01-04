package com.pab.nutritrack.ui.fragment.home

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.pab.nutritrack.R
import com.pab.nutritrack.data.progress.kalori.ProgressKaloriItem
import com.pab.nutritrack.databinding.FragmentHomeBinding
import com.pab.nutritrack.ui.evaluasi.EvaluasiActivity
import com.pab.nutritrack.ui.hidrasi.ProgressHidrasiActivity
import com.pab.nutritrack.ui.rasio.ProgressRasioActivity
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModel }
    private var countTime = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        homeViewModel.apply {
            progressKaloriResponse.observe(viewLifecycleOwner) {
                setProgress(it)
            }
            message.observe(viewLifecycleOwner) {
                clickButtonBackNext(true)
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.apply {
            btnNextDay.setOnClickListener {
                countTime += 1
                if (countTime == 0) {
                    getProgressNow()
                } else {
                    getProgress()
                }
                clickButtonBackNext(false)
            }

            btnBackDay.setOnClickListener {
                countTime -= 1
                if (countTime == 0) {
                    getProgressNow()
                } else {
                    getProgress()
                }
                clickButtonBackNext(false)
            }

            layoutRasio.setOnClickListener {
                startActivity(Intent(requireActivity(), ProgressRasioActivity::class.java))
            }

            layoutHidrasi.setOnClickListener {
                startActivity(Intent(requireActivity(), ProgressHidrasiActivity::class.java))
            }

            layoutAktivitas.setOnClickListener {
                startActivity(Intent(requireActivity(), ProgressRasioActivity::class.java))
            }

            layoutEvaluasi.setOnClickListener {
                startActivity(Intent(requireActivity(), EvaluasiActivity::class.java))
            }

        }

        getProgressNow()
    }

    private fun getProgress() {
        val kal: Calendar = Calendar.getInstance()
        Log.i("countjumlah", countTime.toString())
        kal.add(Calendar.DAY_OF_MONTH, countTime)
        homeViewModel.getProgress(
            "${kal.get(Calendar.YEAR)}-${kal.get(Calendar.MONTH) + 1}-${
                kal.get(
                    Calendar.DAY_OF_MONTH
                )
            }"
        )

        val formatter = SimpleDateFormat("MMM, dd, EEEE", Locale("id", "ID"))
        binding.txtCalender.text = formatter.format(kal.time)
    }

    private fun getProgressNow() {
        val kal: Calendar = Calendar.getInstance()
        homeViewModel.getProgress(
            "${kal.get(Calendar.YEAR)}-${kal.get(Calendar.MONTH) + 1}-${
                kal.get(
                    Calendar.DAY_OF_MONTH
                )
            }"
        )

        val formatter = SimpleDateFormat("MMM, dd, EEEE", Locale("id", "ID"))
        binding.txtCalender.text = formatter.format(kal.time)
    }

    private fun setProgress(data: ProgressKaloriItem) {
        clickButtonBackNext(true)
        binding.apply {
            txtKaloriVal.text = data.target
            txtProteinVal.text = "${data.progressProtein} / ${data.targetProtein} gram"
            txtKarboVal.text = "${data.progressKarbo} / ${data.targetKarbo} gram"
            txtLemakVal.text = "${data.progressLemak} / ${data.targetLemak} gram"

            cpKalori.apply {
                progressMax = data.target.toFloat()
                setProgressWithAnimation(data.progress.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 7f
                progressBarColor = ContextCompat.getColor(requireActivity(), R.color.primary)
            }

            pbProtein.apply {
                max = data.targetProtein.toInt()
                progress = data.progressProtein.toInt()
            }

            pbKarbo.apply {
                max = data.targetKarbo.toInt()
                progress = data.progressKarbo.toInt()
            }

            pbLemak.apply {
                max = data.targetLemak.toInt()
                progress = data.progressLemak.toInt()
            }
        }
    }

    fun clickButtonBackNext(boolean: Boolean) {
        binding.apply {
            btnNextDay.isClickable = boolean
            btnNextDay.isEnabled = boolean
            btnBackDay.isClickable = boolean
            btnBackDay.isEnabled = boolean
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}