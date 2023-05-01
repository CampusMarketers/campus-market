package com.example.sellingapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.sellingapp.Adapters.ItemAdapter
import com.example.sellingapp.databinding.ActivityMainBinding
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private var imageUrl: Uri? = null
    private lateinit var query: Query
    private lateinit var adapter: ItemAdapter
    private lateinit var binding: ActivityMainBinding
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Items")
        query = databaseReference.orderByChild("category")


        binding.cardView1.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView1.text.toString())
            startActivity(intent)
        }
        binding.cardView2.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView2.text.toString())
            startActivity(intent)
        }
        binding.cardView3.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView3.text.toString())
            startActivity(intent)
        }
        binding.cardView4.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView4.text.toString())
            startActivity(intent)
        }
        binding.cardView5.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView5.text.toString())
            startActivity(intent)
        }
        binding.cardView6.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView6.text.toString())
            startActivity(intent)
        }
        binding.cardView7.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView7.text.toString())
            startActivity(intent)
        }
        binding.cardView8.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView8.text.toString())
            startActivity(intent)
        }
        binding.cardView9.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView9.text.toString())
            startActivity(intent)
        }
        binding.cardView10.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("card", binding.textView10.text.toString())
            startActivity(intent)
        }

//        adapter = ItemAdapter(listOf())
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        val profileImageView = headerView.findViewById<ImageView>(R.id.profile_image)
        val profileTextView = headerView.findViewById<TextView>(R.id.userName)

        // Set a click listener on your button
        profileImageView.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > 500) {
                // Perform the action
                lastClickTime = currentTime
                profileImageView.isEnabled = false
                Handler().postDelayed({
                    profileImageView.isEnabled = true
                    //calling profile
                    profile()
                }, 1000)
            }
        }

        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        try {
            database.child("User").child(currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Data exists for the current user, you can retrieve it here
                            val user =
                                snapshot.getValue(UserData::class.java) // Assumes you have a User data class

                            // Do something with the retrieved user data
                            if (user != null) {
                                // Example: Display the user's name in a TextView
                                profileTextView.text = user.userName.toString()
                                Picasso.get()
                                    .load(user.userImage)
                                    .placeholder(R.drawable.router) // Optional placeholder image
//                                .error(R.drawable.error) // Optional error image
                                    .fit()
                                    .centerCrop()
                                    .into(profileImageView)
                            }
                        } else {
                            // Data does not exist for the current user
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                    }
                })

        } catch (e: java.lang.Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }


//        val databaseReference = FirebaseDatabase.getInstance().getReference("Items")
//
//
//        val cat1 = ItemCategory("1","cooler")
//        databaseReference.child("Cooler").setValue(cat1)
//
//        val cat2 = ItemCategory("2","cycle")
//        databaseReference.child("Cycle").setValue(cat2)
//        val cat3 = ItemCategory("3","mattress")
//        databaseReference.child("Mattress").setValue(cat3)
//        val cat4 = ItemCategory("4","drafter")
//        databaseReference.child("Drafter").setValue(cat4)
//        val cat5 = ItemCategory("5","study table")
//        databaseReference.child("Study table").setValue(cat5)
//        val cat6 = ItemCategory("6","router")
//        databaseReference.child("Router").setValue(cat6)
//        val cat7 = ItemCategory("7","electrical appliances")
//        databaseReference.child("Electrical appliances").setValue(cat7)
//        val cat8 = ItemCategory("8","hardware")
//        databaseReference.child("Hardware").setValue(cat8)
//        val cat10 = ItemCategory("10","books")
//        databaseReference.child("Books").setValue(cat10)
//        val cat9 = ItemCategory("9","others")
//        databaseReference.child("Others").setValue(cat9)



//            val databaseReference2 = FirebaseDatabase.getInstance().getReference("items").child(itemId).child("childItem")
//            val itemId2 = databaseReference2.push().key
//
//            if (itemId2 != null) {
//                val item2 = Item(itemId2, "Pulsar".toLowerCase(), "Item Description", )
//                databaseReference2.child(itemId2).setValue(item2)
//
//
//            }
//            val itemId3 = databaseReference2.push().key
//
//            if (itemId3 != null) {
//                val item3 = Item(itemId3, "Apache".toLowerCase(), "Item Description", )
//                databaseReference2.child(itemId3).setValue(item3)
//
//
//            }
        // }
//
//
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query != null) {
//                    searchItems(query.toLowerCase(Locale.getDefault()))
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    searchItems(newText.toLowerCase(Locale.getDefault()))
//                }
//                return true
//            }
//        })
//       try {
//           query.addValueEventListener(object : ValueEventListener {
//               override fun onDataChange(snapshot: DataSnapshot) {
//                   val items = mutableListOf<ItemCategory>()
//                   for (data in snapshot.children) {
//                       val item = data.getValue(ItemCategory::class.java)
//                       if (item != null) {
//                           items.add(item)
//                       }
//                   }
//                   adapter.items = items
//                   adapter.notifyDataSetChanged()
//               }
//
//               override fun onCancelled(error: DatabaseError) {
//                   Log.e("Firebase", error.message)
//               }
//           })
//
//           //start code here on click adapter item
//           adapter.onItemClick = {
//
//               val intent = Intent(this, ItemDetailActivity::class.java)
//               intent.putExtra("item", it)
//
//
//               startActivity(intent)
//
//
//           }
//       }
//       catch (e:java.lang.Exception){
//           Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
//       }
        //end here

        // Set up the ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, binding.mainDrawer, binding.menuBar,
            R.string.app_name, R.string.app_name
        )
        binding.mainDrawer.addDrawerListener(toggle)
        toggle.syncState()

        // Set the NavigationItemSelectedListener
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation view item clicks here
            val id = menuItem.itemId

            when (id) {
                R.id.nav_home -> {
                    // Handle the home action
                }
                R.id.nav_profile -> {
                    // Handle the profile action
                    val intent = Intent(this, NavHeaderActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    // Handle the settings action
                    Toast.makeText(this, "Settings Pressed", Toast.LENGTH_SHORT).show()
                }
                R.id.baseline_sell -> {
                    // Handle the settings action
                    val intent = Intent(this, SellActivity::class.java)
                    startActivity(intent)
                }
                R.id.Chats -> {
                    // Handle the settings action
                    val intent = Intent(this, Customers::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_log_out -> {
                    // Handle the settings action
                    Firebase.auth.signOut()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            // Close the navigation drawer
            binding.mainDrawer.closeDrawer(GravityCompat.START)

            true
        }
    }

    //start searchItem function here
    private fun searchItems(query: String) {
        val searchQuery = databaseReference.orderByChild("category").startAt(query).endAt(
            query.toLowerCase(
                Locale.ROOT
            ) + "\uf8ff"
        )

        searchQuery.addValueEventListener(object : ValueEventListener {
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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", error.message)
            }
        })
    }

    //profile image popUp
    private fun profile() {

        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser

        database.child("User").child(currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Data exists for the current user, you can retrieve it here
                        val user =
                            snapshot.getValue(UserData::class.java) // Assumes you have a User data class

                        // Do something with the retrieved user data
                        if (user != null) {
                            // Example: Display the user's name in a TextView
                            val intent = Intent(this@MainActivity, FullScreenImage::class.java)

                            intent.putExtra("item", user.userImage)
                            startActivity(intent)

                        }
                    } else {
                        // Data does not exist for the current user
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
    }

}
