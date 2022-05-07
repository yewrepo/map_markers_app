package com.example.mapmarkers.repo

import com.example.mapmarkers.marker.MyMarker

interface MarkerRepository {

    fun addMarker(marker: MyMarker)

    fun removeMarker(marker: MyMarker)

    fun getMarkers(): List<MyMarker>
}