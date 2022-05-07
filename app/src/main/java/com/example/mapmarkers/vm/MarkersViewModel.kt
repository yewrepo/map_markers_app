package com.example.mapmarkers.vm

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

    private val _userMarker = MutableLiveData<MyMarker>()
    val userMarker: LiveData<MyMarker>
        get() = _userMarker

    private val _markers = MutableLiveData<List<MyMarker>>()
    val markers: LiveData<List<MyMarker>>
        get() = _markers

    private val _cameraMarker = MutableLiveData<MyMarker>()
    val cameraMarker: LiveData<MyMarker>
        get() = _cameraMarker

    private val _uiState = MutableLiveData(UiState(None, repo.getMarkers()))
    val uiState: LiveData<UiState>
        get() = _uiState

    fun addMarkerClick() {
        postState(UiState(EditMarker, repo.getMarkers()))
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
        postState(UiState(EditMarker, repo.getMarkers()))
    }

    fun editMarker(markerId: Int) {
        repo.getMarkers().find {
            it.id == markerId
        }?.apply {
            editMarker(this)
        }
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

    fun saveCaption(markerId: Int, caption: String) {
        repo.getMarkers().find {
            it.id == markerId
        }?.apply {
            repo.updateMarker(this.copy(caption = caption))
            _uiState.postValue(UiState(None, repo.getMarkers()))
        }
    }

    fun deleteMarker(markerId: Int) {
        repo.getMarkers().find {
            it.id == markerId
        }?.apply {
            repo.removeMarker(this)
            _uiState.postValue(UiState(None, repo.getMarkers()))
        }
    }

    fun loadList() {
        _markers.postValue(repo.getMarkers())
    }

    fun moveCamera(myMarker: MyMarker?) {
        myMarker?.apply {
            _cameraMarker.postValue(this)
        }
    }
}
