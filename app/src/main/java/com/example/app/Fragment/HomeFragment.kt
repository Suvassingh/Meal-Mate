package com.example.app.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.Adapter.MenuAdapter
import com.example.app.Adapter.PopularAdapter
import com.example.app.MenuBottomSheetFragment
import com.example.app.R
import com.example.app.databinding.FragmentHomeBinding
import com.example.app.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class  HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.ViewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        // retrieve and display popular menu items
        retrievePopularMenuItems()
        return binding.root
    }

    private fun retrievePopularMenuItems() {

        //get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        menuItems= mutableListOf()
        //retrieve data from database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //display data in recyclerview
                randomPopularItem()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopularItem() {
        //get random item from the list
        val index=menuItems.indices.toList().shuffled()
        val numItemToShow=10
        val subsetMenuItems=index.take(numItemToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
         val adapter = MenuAdapter(subsetMenuItems,requireContext())
        binding.PopularRecyclerView.adapter = adapter
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }
    companion object {

    }
}