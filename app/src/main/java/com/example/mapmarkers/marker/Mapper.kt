package com.example.mapmarkers.marker

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

fun MyMarker.getOptions(): MarkerOptions {
    return MarkerOptions().also {
        it.position(latLng)
        it.title(caption)
    }
}

fun Marker.toDto() = MyMarker(-1, position, title.orEmpty())