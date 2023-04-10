package com.example.sellingapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.R
import com.example.sellingapp.model.ImageData

class CustomerAdapter(var items: List<ImageData>)
    : RecyclerView.Adapter<CustomerAdapter.ItemViewHolder>() {
    var onItemClick:((ImageData)->Unit)?=null

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.cName)
        // private val lastDate:TextView=itemView.findViewById(R.id.cLastDate)
        // private val lastMessage: TextView = itemView.findViewById(R.id.cLastMessage)
        //private val imageView:ImageView=itemView.findViewById(R.id.cprofile_image)


        fun bind(item: ImageData) {
            nameTextView.text = item.friendUid
//            Picasso.get()
//                .load(item.userImage)
//                .placeholder(R.drawable.profile)
//                .fit()
//                .centerCrop()
//                .into(imageView)

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