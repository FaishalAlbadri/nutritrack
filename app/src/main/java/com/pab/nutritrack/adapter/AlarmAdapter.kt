package com.pab.nutritrack.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pab.nutritrack.R
import com.pab.nutritrack.data.AlarmData
import com.pab.nutritrack.databinding.ItemAlarmBinding
import com.pab.nutritrack.ui.alarm.AlarmDetailActivity
import com.pab.nutritrack.ui.fragment.alarm.AlarmFragment
import java.time.LocalTime

class AlarmAdapter(val recipeList: List<AlarmData>, val alarmFragment: AlarmFragment) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = recipeList.get(position)
            binding.apply {
                txtTitle.text = data.title
                txtTime.text = "${data.hour}:${data.minutes}"
                swAlarm.apply {
                    isChecked = data.status
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        alarmFragment.updateStatus(data.id, swAlarm.isChecked)
                        setTextDay(holder, data)
                    }
                }

                setTextDay(holder, data)

                layout.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            AlarmDetailActivity::class.java
                        ).putExtra("data", data)
                    )
                }

            }
        }
    }

    private fun setTextDay(holder: ViewHolder, data: AlarmData) {
        with(holder) {
            binding.apply {
                txtSenin.apply {
                    if (data.senin) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtSelasa.apply {
                    if (data.selasa) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtRabu.apply {
                    if (data.rabu) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtKamis.apply {
                    if (data.kamis) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtJumat.apply {
                    if (data.jumat) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtSabtu.apply {
                    if (data.sabtu) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }

                txtMinggu.apply {
                    if (data.minggu) {
                        setTextColor(Color.WHITE)
                        if (swAlarm.isChecked) {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_active
                            )
                        } else {
                            background = ContextCompat.getDrawable(
                                alarmFragment.requireContext(),
                                R.drawable.bg_rounded_day_inactive
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}