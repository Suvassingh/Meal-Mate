package com.example.app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ActivityListactivityBinding
import com.example.app.databinding.ListBinding

class ListAdapter(private val context: Context): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=ListBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvTextView22.text="kb coder"
    }
    override fun getItemCount(): Int {
        return 20

    }
    class MyViewHolder(val binding:ListBinding):RecyclerView.ViewHolder(binding.root) {
    }

}