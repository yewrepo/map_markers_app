package com.example.mapmarkers

import com.example.mapmarkers.marker.MyMarker

object None : State()
object ShowList : State()
object SaveMarker : State()
object AddMarker : State()

sealed class State

data class UiState(
    val state: State,
    val myMarkers: List<MyMarker> = emptyList()
)
