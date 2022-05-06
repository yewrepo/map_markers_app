package com.example.mapmarkers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mapmarkers.databinding.FragmentMapBinding
import com.example.mapmarkers.marker.MyMarker
import com.example.mapmarkers.marker.getOptions
import com.example.mapmarkers.repo.MarkerRepositoryImpl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
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

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            Timber.i("state: $state")
            if (state.state is AddMarker) {
                isEditing = true
                showAddMarkerLayout()
            } else {
                isEditing = false
                showCommonLayout(state.myMarkers)
            }
        }
    }

    private fun showCommonLayout(myMarkers: List<MyMarker>) {
        binding.editPanel.isVisible = false
        if (this::map.isInitialized) {
            map.clear()
            myMarkers.forEach {
                map.addMarker(it.getOptions())
            }
        }
    }

    private fun showAddMarkerLayout() {
        viewModel.setEditedMarker(
            map.addMarker(
                MyMarker.empty(map.cameraPosition.target).getOptions()
            )
        )
        binding.apply {
            editPanel.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setPadding(0, 0, 0, 200)
        map.uiSettings.also {
            it.isCompassEnabled = true
            it.isZoomControlsEnabled = true
        }

        map.setOnCameraChangeListener {
            if (isEditing) {
                Timber.e("moving")
                map.clear()
                viewModel.setEditedMarker(map.addMarker(MyMarker.empty(it.target).getOptions()))
            }
        }
    }
}