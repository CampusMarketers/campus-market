package com.example.sellingapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sellingapp.Adapters.ItemAdapter
import com.example.sellingapp.databinding.ActivityCustomersBinding
import com.example.sellingapp.model.ItemCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Customers : AppCompatActivity() {

    private lateinit var binding: ActivityCustomersBinding
    private lateinit var db: DatabaseReference
    private lateinit var db2: DatabaseReference
    private lateinit var adapter: ItemAdapter
    private lateinit var query:Query
    private lateinit var query2:Query


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Friends")
        query = db.orderByChild("category")

        adapter = ItemAdapter(listOf())
        binding.customerRecycler.adapter = adapter
        binding.customerRecycler.layoutManager = LinearLayoutManager(this)
        try {
            query.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = mutableListOf<ItemCategory>()

                    for (data in snapshot.children) {
                        val item = data.getValue(ItemCategory::class.java)
                        if (item != null) {
                            items.add(item)

                        }
                    }

                    adapter.items = items
                    adapter.notifyDataSetChanged()
                    // Toast.makeText(this@Customers,"success",Toast.LENGTH_LONG).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", error.message)
                }
            })

            // start code here on click adapter item
            adapter.onItemClick = {
                       val intent = Intent(this, OwnerChat::class.java)
                       intent.putExtra("item", it)
                       startActivity(intent)
            }
        }
        catch (e:java.lang.Exception){
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
        binding.bckBtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}