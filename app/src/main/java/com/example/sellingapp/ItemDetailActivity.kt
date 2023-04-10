package com.example.sellingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.Adapters.GridAdapter
import com.example.sellingapp.databinding.ActivityItemDetailBinding
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserItemData
import com.google.firebase.database.*

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var query: Query
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter: GridAdapter
    private lateinit var binding: ActivityItemDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getStringExtra("card")

        if (item != null) {

            binding.tvName.text = item.toString().capitalize()
//            binding.tvDescription.text = item.catUid

            var catName: String = item.toString().capitalize()

            try {
                databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(catName).child("childItem")
                query = databaseReference.orderByChild("itemName")

                recyclerView = findViewById(R.id.childRecycler)


                adapter = GridAdapter(listOf())
                binding.childRecycler.adapter = adapter
                binding.childRecycler.layoutManager = GridLayoutManager(this, 2)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = mutableListOf<UserItemData>()
                        for (data in snapshot.children) {
                            val item = data.getValue(UserItemData::class.java)
                            if (item != null) {
                                items.add(item)
                            }
                        }
                        adapter.items = items
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", error.message)
                    }
                })
                adapter.onItemClick = {

                    val intent = Intent(this, OrderPage::class.java)
                    intent.putExtra("item", it)
                    startActivity(intent)

                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }
        binding.backBtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}
