package com.pab.nutritrack.utils.picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pab.nutritrack.databinding.ItemSliderBinding

class SliderAdapter() : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    private val data: ArrayList<String> = ArrayList();
    var callback: Callback? = null
    val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }

    inner class ViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderAdapter.ViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(clickListener)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderAdapter.ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                tvItem.text = data[position]
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }
}