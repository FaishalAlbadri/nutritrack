package com.pab.nutritrack.ui.fragment.alarm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pab.nutritrack.R
import com.pab.nutritrack.adapter.AlarmAdapter
import com.pab.nutritrack.data.AlarmData
import com.pab.nutritrack.databinding.FragmentAlarmBinding
import com.pab.nutritrack.ui.alarm.AlarmDetailActivity
import com.pab.nutritrack.utils.viewmodel.ViewModelFactory
import java.time.LocalTime

class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val alarmViewModel: AlarmViewModel by viewModels { viewModel }
    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        alarmViewModel.apply {
            alarmData.observe(viewLifecycleOwner) {
                setAlarm(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnAdd.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    AlarmDetailActivity::class.java
                ).putExtra(
                    "data",
                    AlarmData(
                        0,
                        "",
                        LocalTime.now().hour,
                        LocalTime.now().minute,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        true
                    )
                )
            )
        }
    }

    private fun setAlarm(it: List<AlarmData>) {
        alarmAdapter = AlarmAdapter(it, this)
        binding.rvAlarm.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alarmAdapter
        }
    }

    fun updateStatus(id: Int, boolean: Boolean) {
        alarmViewModel.updateStatus(id, boolean)
    }

    override fun onResume() {
        super.onResume()
        alarmViewModel.getAlarm()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}