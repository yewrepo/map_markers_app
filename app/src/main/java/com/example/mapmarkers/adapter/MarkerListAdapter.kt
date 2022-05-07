package com.example.mapmarkers.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mapmarkers.marker.MyMarker

private val diffCallback = object : DiffUtil.ItemCallback<MyMarker>() {

    override fun areItemsTheSame(oldItem: MyMarker, newItem: MyMarker) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MyMarker, newItem: MyMarker) = oldItem == newItem
}

class MarkerListAdapter(
    private var callback: Callback
) : ListAdapter<MyMarker, MyMarkerViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyMarkerViewHolder.create(parent, callback)

    override fun onBindViewHolder(holder: MyMarkerViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }
}