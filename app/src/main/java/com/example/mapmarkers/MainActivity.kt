package com.example.mapmarkers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.activity.viewModels
import com.example.mapmarkers.databinding.ActivityMainBinding
import com.example.mapmarkers.repo.MarkerRepositoryImpl
import com.example.mapmarkers.vm.EditMarker
import com.example.mapmarkers.vm.MarkersViewModel
import com.example.mapmarkers.vm.ViewModelFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MarkersViewModel> {
        ViewModelFactory(MarkerRepositoryImpl(), application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            viewModel.addMarkerClick()
        }

        viewModel.uiState.observe(this) { state ->
            Timber.i("state: ${state.state}")
            if (state.state is EditMarker) {
                binding.fab.hide()
            } else {
                binding.fab.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}