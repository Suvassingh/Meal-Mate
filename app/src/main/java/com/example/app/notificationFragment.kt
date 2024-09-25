package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.Adapter.NotificationAdapter

import com.example.app.databinding.FragmentNotificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class notificationFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentNotificationBinding
    private lateinit var adapter: NotificationAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView() {
        val notification= arrayListOf("Your order has been Canceled Successfully", "Order has been taken by the driver", "Congrats Your Order Placed")
        val notificationImage = arrayListOf(
            R.drawable.sademoji,
            R.drawable.truck,
            R.drawable.illustration,
        )
        adapter = NotificationAdapter(notification, notificationImage)
        binding.notificationrecyclerView.adapter = adapter
        binding.notificationrecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {

    }
}