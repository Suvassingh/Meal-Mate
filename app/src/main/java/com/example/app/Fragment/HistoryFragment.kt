package com.example.app.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.app.Adapter.BuyAgainAdapter
import com.example.app.RecentOrderItem
import com.example.app.databinding.FragmentHistoryBinding
import com.example.app.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class  HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId:String
    private var listOfOrderItem:MutableList<OrderDetails> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        //initialize database and auth
        database=FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()
        userId=auth.currentUser?.uid.toString()
        //retrieve order details from firebase and display it
        retrieveBuyHistory()
        //recent buy botton click
        binding.recentBuyItem.setOnClickListener {
            seeItemRecentBuy()
        }
        binding.recieved.setOnClickListener{
            updateOrderStatus()
        }

        return binding.root

    }
    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentRecieved").setValue(true)
    }


// function to see items recent buys

    private fun seeItemRecentBuy() {
        listOfOrderItem.firstOrNull()?.let {
            recentbuy->
            val intent = Intent(requireContext(),RecentOrderItem::class.java)
            //intent.putExtras("RecentBuyOrderItem",listOfOrderItem)
            intent.putExtra("RecentBuyOrderItem",ArrayList(listOfOrderItem))
            startActivity(intent)
        }

    }

// function to retrieve items buy history

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility=View.INVISIBLE
        userId=auth.currentUser?.uid?:""
        val buyItemReference=database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery=buyItemReference.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()){
                    //display the most recent order item
                    setDataInRecentBuyItem()
                    //set up the recycler view for previous buy item
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
//function to display most recent order details
    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility=View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                buyAgainFoodName.text=it.foodNames?.firstOrNull()?:""
                buyAgainFoodPrice.text=it.foodPrices?.firstOrNull()?:""
                val image = it.foodImages?.firstOrNull()?:""
                val uri= Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)
                val isOrderIsAccepted=listOfOrderItem[0].orderAccepted
                if(isOrderIsAccepted){
                    orderStatus.background.setTint(Color.GREEN)
                    recieved.visibility=View.VISIBLE

                }
            }
        }
    }
    //function to setup the  recyclerview with previous order detail


    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName= mutableListOf<String>()
        val buyAgainFoodPrice= mutableListOf<String>()
        val buyAgainFoodImage= mutableListOf<String>()
            for (i in 1 until listOfOrderItem.size){
                listOfOrderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
                listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
                listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
            }
            val rv = binding.buyAgainRecyclerView
        rv.layoutManager=LinearLayoutManager(requireContext())
        buyAgainAdapter=BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
        rv.adapter=buyAgainAdapter
    }
}



