package com.example.mapmarkers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mapmarkers.databinding.FragmentMapBinding
import com.example.mapmarkers.marker.MyMarker
import com.example.mapmarkers.marker.addTagMarker
import com.example.mapmarkers.marker.getOptions
import com.example.mapmarkers.repo.MarkerRepositoryImpl
import com.example.mapmarkers.vm.EditMarker
import com.example.mapmarkers.vm.MarkersViewModel
import com.example.mapmarkers.vm.SaveMarker
import com.example.mapmarkers.vm.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import timber.log.Timber

class MapFragment : Fragment(), OnMapReadyCallback {

    private var isEditing: Boolean = false
    private lateinit var map: GoogleMap

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val factory by lazy {
        ViewModelFactory(
            MarkerRepositoryImpl(), requireActivity().application
        )
    }

    private val viewModel by activityViewModels<MarkersViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = binding.map.getFragment<SupportMapFragment>()
        mapFragment.getMapAsync(this)

        binding.saveButton.setOnClickListener {
            viewModel.saveMarker()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.cancelEdit()
        }

        viewModel.userMarker.observe(viewLifecycleOwner) { marker ->
            map.addMarker(marker.getOptions())
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Timber.i("state: $state")

            when (state.state) {
                is EditMarker -> {
                    isEditing = true
                    showAddMarkerLayout()
                }
                is SaveMarker -> {
                    isEditing = false
                    showEditDialog(state.myMarkers)
                }
                else -> {
                    isEditing = false
                    showCommonLayout(state.myMarkers)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        map.setOnInfoWindowClickListener(null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setPadding(0, 0, 0, 200)
        map.uiSettings.also {
            it.isCompassEnabled = true
            it.isZoomControlsEnabled = true
        }
        map.setOnInfoWindowClickListener {
            Timber.i("on marker ${it.tag} click")
            viewModel.editMarker(it.tag as MyMarker)
        }
        map.setOnCameraChangeListener { position ->
            if (isEditing) {
                map.clear()
                viewModel.uiState.value?.myMarkers?.onEach { map.addTagMarker(it) }
                viewModel.setUserMarker(MyMarker.empty(position.target))
            }
        }
    }

    private fun showEditDialog(myMarkers: List<MyMarker>) {

    }

    private fun showCommonLayout(myMarkers: List<MyMarker>) {
        binding.editPanel.isVisible = false
        if (this::map.isInitialized) {
            map.clear()
            myMarkers.forEach { map.addTagMarker(it) }
        }
    }

    private fun showAddMarkerLayout() {
        binding.editPanel.isVisible = true
        val newMarker = MyMarker.empty(map.cameraPosition.target)
        map.addMarker(newMarker.getOptions())
        viewModel.editMarker(newMarker)
    }

}