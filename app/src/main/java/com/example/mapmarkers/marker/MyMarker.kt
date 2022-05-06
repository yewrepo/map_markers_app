package com.example.mapmarkers.marker

import com.google.android.gms.maps.model.LatLng

data class MyMarker(
    val id: Int,
    val latLng: LatLng,
    val caption: String
) {

    companion object {

        @JvmStatic
        fun empty(position: LatLng) = MyMarker(-1, position, "New Marker!")
    }
}
