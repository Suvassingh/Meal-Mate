package com.example.app.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.app.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartAdapter(
    private val context: Context,
    private val CartItems: MutableList<String>,
    private val CartItemPrices: MutableList<String>,
    private val CartDescription:MutableList<String>,
    private val CartImage: MutableList<String>,

    private val CartQuantity:MutableList<Int>,
    private var CartIngridients:MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    //instance Firebase
    private val auth=FirebaseAuth.getInstance()
    init {
        val database=FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber:Int=CartItems.size
        itemQuantities=IntArray(cartItemNumber){1}
        cartItemsReference=database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities:IntArray= intArrayOf()
        private lateinit var cartItemsReference:DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(CartItems[position], CartItemPrices[position], CartImage[position],CartDescription[position],CartQuantity[position])
    }

    override fun getItemCount(): Int = CartItems.size
    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(CartItems: String, CartItemPrices: String, CartImage: String, s: String, i: Int) {
            binding.apply {

                cartFoodName.text = CartItems
                cartItemPrice.text = CartItemPrices
                //load image using Glide
                val uriString =CartImage
                val uri =Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)
                itemQuantities.forEach { cartItemQuantity }
                minusButton.setOnClickListener {
                    decreaseQuantity(adapterPosition)

                }
                plusButton.setOnClickListener {
                    increaseQuantity(adapterPosition)

                }
                deleteButtom.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }

                }
                


            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                CartQuantity[position]= itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                CartQuantity[position]= itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
           val positionRetrieve=position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
                if (uniqueKey !=null){
                    removeItem(position,uniqueKey)
                    binding
                }
            }

        }
    }

    private fun removeItem(position: Int, uniqueKey: String) {
        if (uniqueKey != null){
            cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                CartItems.removeAt(position)
                CartItemPrices.removeAt(position)
                CartImage.removeAt(position)
                CartDescription.removeAt(position)
                CartQuantity.removeAt(position)
                CartIngridients.removeAt(position)
                Toast.makeText(context, "Item Delete", Toast.LENGTH_SHORT).show()

                //update itemQuantities
                itemQuantities= itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,CartItems.size)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete:(String?)->Unit) {
        cartItemsReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey:String?=null
                //loop for snapshot children
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index==positionRetrieve){
                        uniqueKey=dataSnapshot.key
                        return@forEachIndexed

                    }else{}
                }
                onComplete(uniqueKey)


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
//get updated quantity
    fun getUpdateItemsQuantities(): MutableList<Int> {
        val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(CartQuantity)
        return itemQuantity

    }

}