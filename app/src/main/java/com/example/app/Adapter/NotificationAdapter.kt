package com.example.app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.NotificationItemBinding
import com.example.app.databinding.FragmentProfileBinding


import java.util.ArrayList

class NotificationAdapter(private var notification:ArrayList<String>,private var notificationImage:ArrayList<Int>): RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notification[position],notificationImage[position])
    }
    override fun getItemCount(): Int =notification.size
    inner class NotificationViewHolder( private val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: String, notificationImage: Int) {
            binding.notificationTextView.text=notification
            binding.notificationImageView.setImageResource(notificationImage)
        }

    }
}