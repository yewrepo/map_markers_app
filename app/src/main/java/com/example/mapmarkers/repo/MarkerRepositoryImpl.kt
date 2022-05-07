package com.example.mapmarkers.repo

import com.example.mapmarkers.marker.MyMarker
import timber.log.Timber

class MarkerRepositoryImpl : MarkerRepository {

    private val markerList = mutableListOf<MyMarker>()

    override fun addMarker(marker: MyMarker) {
        markerList.add(marker.copy(id = getMarkers().size))
    }

    override fun removeMarker(marker: MyMarker) {
        val isSuccess = markerList.remove(marker)
        Timber.i("removeMarker: $isSuccess")
    }

    override fun getMarkers() = markerList
}