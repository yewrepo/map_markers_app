package com.example.mapmarkers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapmarkers.marker.MyMarker
import com.example.mapmarkers.marker.toDto
import com.example.mapmarkers.repo.MarkerRepository
import com.google.android.gms.maps.model.Marker

class MarkersViewModel(
    private val repo: MarkerRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _editableMarker = MutableLiveData<MyMarker>()
    val editableMarker: LiveData<MyMarker>
        get() = _editableMarker

    private val _uiState = MutableLiveData(UiState(None, repo.getMarkers()))
    val uiState: LiveData<UiState>
        get() = _uiState

    fun addMarkerClick() {
        _uiState.postValue(UiState(AddMarker))
    }

    fun cancelEdit() {
        _uiState.postValue(UiState(None, repo.getMarkers()))
    }

    fun setEditedMarker(marker: Marker?) {
        marker?.apply {
            _editableMarker.postValue(this.toDto())
        }
    }

    fun saveMarker() {
        _editableMarker.value?.apply {
            repo.addMarker(this);
            _uiState.postValue(UiState(None, repo.getMarkers()))
        }
    }
}
