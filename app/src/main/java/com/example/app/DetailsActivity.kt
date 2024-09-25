package com.example.app

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.app.databinding.ActivityDetailsBinding
import com.example.app.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private val binding: ActivityDetailsBinding by lazy {
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodIngridient: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initializing auth in database
        auth=FirebaseAuth.getInstance()
        foodName = intent.getStringExtra("MenuItemName")
        foodIngridient = intent.getStringExtra("MenuItemIngridient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodImage = intent.getStringExtra("MenuItemImage")
        with(binding) {
            detailFoodname.text = foodName
            deatilDescription.text = foodDescription
            detailIngridients.text = foodIngridient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailfoodimage)
            binding.imageButton.setOnClickListener{
                finish()
            }

        }
        binding.addItemBotton.setOnClickListener{
            addItemToCart()


        }


    }

    private fun addItemToCart() {
    val database=FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""
        // create a cart item object
        val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)
        //save data to cart item to database
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added To Cart Successfully...", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item Add To Cart Failed...", Toast.LENGTH_SHORT).show()


        }


    }
}