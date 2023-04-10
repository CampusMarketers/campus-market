package com.example.sellingapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.model.UserItemData
import com.example.sellingapp.R


import com.squareup.picasso.Picasso

class Grid2Adapter(var items: List<UserItemData>) : RecyclerView.Adapter<Grid2Adapter.ViewHolder>() {
    var onItemClick:((UserItemData)->Unit)?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)

        }


//        Picasso.get()
//            .load(item.itemImageId)
//            .placeholder(R.drawable.images)
//            .fit()
//            .centerCrop()
//            .into(holder.imageView)
//        holder.textView.text = item.itemName
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
        fun bind(item: UserItemData) {
            textView.text = item.itemName

            Picasso.get()
                .load(item.itemImageId)
                .placeholder(R.drawable.ic_profile)
                .fit()
                .centerCrop()
                .into(imageView)
            //     descriptionTextView.text = item.catUid
        }
    }
}