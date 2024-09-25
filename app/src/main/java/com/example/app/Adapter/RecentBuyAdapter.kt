package com.example.app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.databinding.RecentbuyitemBinding

class RecentBuyAdapter(private var context: Context,
    private var foodNameList:ArrayList<String>,
    private var foodImageList:ArrayList<String>,
    private var foodPriceList:ArrayList<String>,
    private var foodQuantityList:ArrayList<Int>,

    ):RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding=RecentbuyitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentViewHolder(binding)
    }

    override fun getItemCount(): Int =foodNameList.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class RecentViewHolder(val binding: RecentbuyitemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                recentOrderFoodName.text=foodNameList[position]
                recentOrderFoodPrice.text=foodPriceList[position]
                recentOrderFoodQuantity.text=foodQuantityList[position].toString()
                val uriString=foodImageList[position]
                val uri =Uri.parse(uriString)
                Glide.with(context).load(uri).into(recentOrderImage)
            }
        }


    }
}