package com.example.app.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.DetailsActivity
import com.example.app.databinding.PopularItemBinding
import com.example.app.databinding.FragmentHomeBinding

import java.util.ArrayList

class PopularAdapter(private val items:ArrayList<String>,private val prices:ArrayList<String>,private val images:ArrayList<Int>,private val requireContext: Context): RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    private val itemClickListener: OnClickListener?=null

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding=PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularViewHolder(binding)
    }


    override fun getItemCount(): Int=items.size

   inner  class PopularViewHolder(private val binding: PopularItemBinding):RecyclerView.ViewHolder(binding.root) {
       init{
           binding.root.setOnClickListener {
               val position=adapterPosition
               if (position!=RecyclerView.NO_POSITION){
                   itemClickListener?.onItemClick(position)
               }
               // set on click listener to open details
               val intent= Intent(requireContext,DetailsActivity::class.java)
               intent.putExtra("menuItemsName",items.get(position))
               intent.putExtra("menuItemsImage",images.get(position))
               requireContext.startActivity(intent)
           }
       }
       fun bind(position: Int) {
           binding.apply {
               foodNamePopuler.text=items[position]
               PricePopular.text=prices[position]
               Image.setImageResource(images[position])

           }
       }
   }
}

private fun Any?.onItemClick(position: Int) {

}
