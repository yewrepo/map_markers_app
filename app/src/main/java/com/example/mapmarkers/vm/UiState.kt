package com.example.mapmarkers.vm

import com.example.mapmarkers.marker.MyMarker

object None : State()
object SaveMarker : State()
object EditMarker : State()

sealed class State

data class UiState(
    val state: State,
    val myMarkers: List<MyMarker> = emptyList()
)
