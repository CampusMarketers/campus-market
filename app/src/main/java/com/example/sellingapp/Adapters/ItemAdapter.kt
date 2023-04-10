package com.example.sellingapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.R
import com.example.sellingapp.model.ItemCategory
import com.squareup.picasso.Picasso

class ItemAdapter(var items: List<ItemCategory>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var onItemClick:((ItemCategory)->Unit)?=null

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val nameTextView: TextView = itemView.findViewById(R.id.cName)
         val imageView: ImageView = itemView.findViewById(R.id.cprofile_image)

        fun bind(item: ItemCategory) {
            nameTextView.text = item.category
            //     descriptionTextView.text = item.catUid
            Picasso.get()
                .load(item.catPicUrl)
                .placeholder(R.drawable.profile)
                .fit()
                .centerCrop()
                .into(imageView)
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