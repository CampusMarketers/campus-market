package com.example.sellingapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.R
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserData
import com.example.sellingapp.model.lastmessageAndTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ItemAdapter(var items: List<ItemCategory>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var onItemClick:((ItemCategory)->Unit)?=null


    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val nameTextView: TextView = itemView.findViewById(R.id.cName)
         val imageView: ImageView = itemView.findViewById(R.id.cprofile_image)
         val lastMessage:TextView=itemView.findViewById(R.id.cLastMessage)
        val lastTime:TextView=itemView.findViewById(R.id.date)
        fun bind(item: ItemCategory) {
            nameTextView.text = item.category
            //     descriptionTextView.text = item.catUid
            Picasso.get()
                .load(item.catPicUrl)
                .placeholder(R.drawable.profile)
                .fit()
                .centerCrop()
                .into(imageView)

           var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("chats")
               .child(FirebaseAuth.getInstance().currentUser!!.uid + item.catUid)

            try {
                database
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // Data exists for the current user, you can retrieve it here
                                val user =
                                    snapshot.getValue(lastmessageAndTime::class.java) // Assumes you have a User data class

                                // Do something with the retrieved user data
                                if (user != null) {
                                    // Example: Display the user's name in a TextView
                                    lastMessage.text = user.lastMsg.toString()
                                    val timeStamp = user.lastMsgTime
                                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val date = Date(timeStamp)
                                    val formattedDate = sdf.format(date)
                                    lastTime.text = formattedDate
                                }

                            }
                            else {
                                // Data does not exist for the current user
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })

            } catch (e: java.lang.Exception) {
            //    Toast.makeText(this@ItemAdapter, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.each_customer, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}