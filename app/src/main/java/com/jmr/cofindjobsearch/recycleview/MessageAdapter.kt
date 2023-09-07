package com.jmr.cofindjobsearch.recycleview

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.fragments.Create_Job
import com.jmr.cofindjobsearch.services.Utils

class MessageAdapter(var items: ArrayList<MessageData>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_messaging,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            tvSenderMessage.text = item.message
            tvReceiverMessage.text = item.message

            if (item.isMine == 1) {
                lnSenderMessage.visibility = View.GONE
                lnReceiverMessage.visibility = View.VISIBLE
            } else {
                lnSenderMessage.visibility = View.VISIBLE
                lnReceiverMessage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val lnSenderMessage : LinearLayout = itemView.findViewById(R.id.lnSenderMessage)
        val tvSenderMessage : TextView = itemView.findViewById(R.id.tvSenderMessage)
        val lnReceiverMessage : LinearLayout = itemView.findViewById(R.id.lnReceiverMessage)
        val tvReceiverMessage : TextView = itemView.findViewById(R.id.tvReceiverMessage)
    }
}