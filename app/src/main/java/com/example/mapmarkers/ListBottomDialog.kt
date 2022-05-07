package com.example.mapmarkers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapmarkers.adapter.Callback
import com.example.mapmarkers.adapter.MarkerListAdapter
import com.example.mapmarkers.databinding.DialogBottomListBinding
import com.example.mapmarkers.repo.MarkerRepositoryImpl
import com.example.mapmarkers.vm.MarkersViewModel
import com.example.mapmarkers.vm.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ListBottomDialog : BottomSheetDialogFragment() {

    private lateinit var adapter: MarkerListAdapter
    private lateinit var binding: DialogBottomListBinding

    private val factory by lazy {
        ViewModelFactory(
            MarkerRepositoryImpl(), requireActivity().application
        )
    }

    private val viewModel by activityViewModels<MarkersViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBottomListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MarkerListAdapter(object : Callback {
            override fun click(pos: Int) {
                dismissAllowingStateLoss()
                viewModel.moveCamera(adapter.currentList[pos])
            }
        })

        binding.recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter

        viewModel.markers.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadList()
    }

    companion object {

        const val TAG: String = "ListBottomDialog"

        @JvmStatic
        fun getInstance() = ListBottomDialog()
    }
}