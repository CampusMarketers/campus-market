package com.example.sellingapp.Adapters

//import android.app.AlertDialog
//import android.content.Context
import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import com.example.sellingapp.model.messageModel
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.sellingapp.R
import com.google.firebase.auth.FirebaseAuth
import android.view.View.OnLongClickListener
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.sellingapp.FullScreenImage
import com.example.sellingapp.databinding.DeleteBinding
import com.example.sellingapp.databinding.ReceiverBinding
import com.example.sellingapp.databinding.SenderBinding
import com.example.sellingapp.model.ItemCategory
import com.example.sellingapp.model.UserItemData
import com.squareup.picasso.Picasso

class ChatAdapter(
    var context: Context,
    messages: ArrayList<messageModel>?,
    senderRoom: String,
    receiverRoom: String,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    lateinit var messages: ArrayList<messageModel>

    var onItemClick:((messageModel)->Unit)?=null

    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    val senderRoom: String
    var receiverRoom: String
    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == messages.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SenderBinding = SenderBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ReceiverBinding = ReceiverBinding.bind(itemView)
    }

    init {
        if (messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false)
            SentMsgHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.receiver, parent, false)
            ReceiveMsgHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            val viewHolder = holder as SentMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Picasso.get()
                    .load(message.imageUrl)
                    .placeholder(R.drawable.images) // Optional placeholder image
//                                .error(R.drawable.error) // Optional error image
                    .fit()
                    .centerCrop()
                    .into(viewHolder.binding.image)

                viewHolder.itemView.setOnClickListener {
                    onItemClick?.invoke(message)
                }
            } else {
                viewHolder.binding.image.visibility = View.GONE
                viewHolder.binding.message.visibility = View.VISIBLE
                viewHolder.binding.mLinear.visibility = View.VISIBLE
                viewHolder.binding.message.text = message.message



                viewHolder.itemView.setOnLongClickListener {
                    val view = LayoutInflater.from(context).inflate(R.layout.delete, null)
                    val binding: DeleteBinding = DeleteBinding.bind(view)
                    val dialog: AlertDialog = AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.root)
                        .create()
                    binding.everyone.setOnClickListener {
                        message.message = "This message is removed"
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(it).setValue(message)
                        }
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(it).setValue(message)
                        }
                        dialog.dismiss()
                    }

                    binding.delete.setOnClickListener {
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(it).setValue(null)
                        }
                        dialog.dismiss()

                    }
                    binding.cancel.setOnClickListener { dialog.dismiss() }
                    dialog.show()

                    false
                }
            }
        } else {
            val viewHolder = holder as ReceiveMsgHolder
            if (message.message.equals("photo")) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.GONE
                viewHolder.binding.mLinear.visibility = View.GONE
                Picasso.get()
                    .load(message.imageUrl)
                    .placeholder(R.drawable.images) // Optional placeholder image
//                                .error(R.drawable.error) // Optional error image
                    .fit()
                    .centerCrop()
                    .into(viewHolder.binding.image)

                viewHolder.itemView.setOnClickListener {
                    onItemClick?.invoke(message)
                }
            } else {
                viewHolder.binding.image.visibility = View.GONE
                viewHolder.binding.message.visibility = View.VISIBLE
                viewHolder.binding.mLinear.visibility = View.VISIBLE
                viewHolder.binding.message.text = message.message
                viewHolder.itemView.setOnLongClickListener {
                    val view = LayoutInflater.from(context).inflate(R.layout.delete, null)
                    val binding: DeleteBinding = DeleteBinding.bind(view)
                    val dialog: AlertDialog = AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setView(binding.root)
                        .create()
                    binding.everyone.setOnClickListener {
                        message.message = "This message is removed"
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(it).setValue(message)
                        }
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(receiverRoom)
                                .child("message")
                                .child(it).setValue(message)
                        }
                        dialog.dismiss()
                    }

                    binding.delete.setOnClickListener {
                        message.messageId?.let {
                            FirebaseDatabase.getInstance().reference.child("chats")
                                .child(senderRoom)
                                .child("message")
                                .child(it).setValue(null)
                        }
                        dialog.dismiss()

                    }
                    binding.cancel.setOnClickListener { dialog.dismiss() }
                    dialog.show()

                    false
                }
            }

        }
    }

}