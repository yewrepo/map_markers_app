package com.example.mapmarkers.repo

import com.example.mapmarkers.marker.MyMarker

class MarkerRepositoryImpl : MarkerRepository {

    private val markerList = mutableListOf<MyMarker>()

    override fun addMarker(marker: MyMarker) {
        markerList.add(marker.copy(id = getMarkers().size))
    }

    override fun getMarkers() = markerList
}