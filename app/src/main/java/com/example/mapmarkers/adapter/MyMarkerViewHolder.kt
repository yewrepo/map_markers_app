package com.example.mapmarkers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapmarkers.databinding.ViewHolderMyMarkerBinding
import com.example.mapmarkers.marker.MyMarker

class MyMarkerViewHolder(
    private val binding: ViewHolderMyMarkerBinding,
    private val callback: Callback
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.goButton.setOnClickListener {
            callback.click(adapterPosition)
        }
    }

    fun bind(marker: MyMarker?) {
        marker?.apply {
            binding.caption.text = "${this.id}: ${this.caption} "
        }
    }

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, callback: Callback): MyMarkerViewHolder {
            val binding = ViewHolderMyMarkerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return MyMarkerViewHolder(binding, callback)
        }
    }
}