package com.example.sellingapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sellingapp.model.Item
import com.example.sellingapp.R

class ImageAdapter(var items: List<Item>)
    : RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {
    var onItemClick:((Item)->Unit)?=null

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        //private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(item: Item) {
            nameTextView.text = item.name
            //   descriptionTextView.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sample_item, parent, false)
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