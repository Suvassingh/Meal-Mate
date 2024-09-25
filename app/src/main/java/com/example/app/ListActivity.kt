package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.Adapter.ListAdapter
import com.example.app.databinding.ActivityListactivityBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityListactivityBinding
    private lateinit var adapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listrecyclerview.layoutManager=LinearLayoutManager(this)
        adapter=ListAdapter(this)
        binding.listrecyclerview.adapter=adapter


    }
}