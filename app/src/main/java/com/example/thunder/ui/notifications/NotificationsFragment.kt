package com.example.thunder.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.thunder.R
import com.example.thunder.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private val viewModel: NotificationsViewModel by viewModels()

    lateinit var alertAdapter: AlertAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addNewAlert.setOnClickListener {
            AddingNewAlertDialog().show(childFragmentManager, "NewAlertDialog")
        }

        alertAdapter = AlertAdapter(arrayListOf(), viewModel)
        initAlertsRecycler()

        viewModel.getAlerts().asLiveData().observe(viewLifecycleOwner) {
            alertAdapter.changeData(it)
        }

        return root
    }

    private fun initAlertsRecycler() {
        binding.alertsRecyclerView.apply {
            adapter = alertAdapter
        }
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
                alertAdapter.removeSelectedAlertFromAdapter(viewHolder)
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.alertsRecyclerView)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}