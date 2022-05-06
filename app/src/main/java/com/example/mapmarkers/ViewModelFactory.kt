package com.example.mapmarkers

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mapmarkers.repo.MarkerRepository

class ViewModelFactory(
    private val repo: MarkerRepository,
    private val app: Application,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MarkersViewModel::class.java) -> {
                MarkersViewModel(repo, app) as T
            }
            else -> throw IllegalStateException("")
        }
    }
}