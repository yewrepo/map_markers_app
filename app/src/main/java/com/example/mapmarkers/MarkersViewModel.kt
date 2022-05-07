package com.example.mapmarkers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapmarkers.marker.MyMarker
import com.example.mapmarkers.repo.MarkerRepository

class MarkersViewModel(
    private val repo: MarkerRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _editableMarker = MutableLiveData<MyMarker>()
    val editableMarker: LiveData<MyMarker>
        get() = _editableMarker

    private val _userMarker = MutableLiveData<MyMarker>()
    val userMarker: LiveData<MyMarker>
        get() = _userMarker

    private val _uiState = MutableLiveData(UiState(None, repo.getMarkers()))
    val uiState: LiveData<UiState>
        get() = _uiState

    fun addMarkerClick() {
        postState(UiState(EditMarker))
    }

    fun cancelEdit() {
        _editableMarker.value?.apply {
            if (id >= 0) {
                repo.addMarker(this)
            }
            postState(UiState(None, repo.getMarkers()))
        }
    }

    fun saveMarker() {
        _userMarker.value?.apply {
            repo.addMarker(this)
            postState(UiState(None, repo.getMarkers()))
        }
    }

    fun editMarker(myMarker: MyMarker) {
        repo.removeMarker(myMarker)
        _editableMarker.postValue(myMarker)
        postState(UiState(EditMarker))
    }

    private fun postState(state: UiState) {
        if (_uiState.value != state) {
            _uiState.postValue(state)
        }
    }

    fun setUserMarker(empty: MyMarker) {
        _editableMarker.value?.apply {
            _userMarker.postValue(this.copy(latLng = empty.latLng))
        }
    }
}
