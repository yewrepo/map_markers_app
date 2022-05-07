package com.example.mapmarkers.repo

import com.example.mapmarkers.marker.MyMarker

class MarkerRepositoryImpl : MarkerRepository {

    private val markerList = mutableListOf<MyMarker>()

    override fun addMarker(marker: MyMarker) {
        markerList.add(marker.copy(id = getMarkers().size))
    }

    override fun removeMarker(marker: MyMarker) {
        markerList.remove(marker)
    }

    override fun getMarkers() = markerList

    override fun updateMarker(marker: MyMarker) {
        markerList
            .replaceAll {
                return@replaceAll if (it.id == marker.id) {
                    it.copy(caption = marker.caption)
                } else {
                    it
                }
            }
    }
}