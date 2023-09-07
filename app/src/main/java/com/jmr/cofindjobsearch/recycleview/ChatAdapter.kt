package com.jmr.cofindjobsearch.recycleview

import android.annotation.SuppressLint
import android.content.Intent
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
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.fragments.Create_Job
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.Utils

class ChatAdapter(var items: ArrayList<ChatData>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_chat,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(Uri.parse(item.imageLink))
                .into(imgProfile)

            tvFullName.text = item.contact_name
            tvMessage.text = item.last_message

            lnRowChat.setOnClickListener {
                SharedHelper.apply {
                    putInt("receiver_id",item.sender_id)
                    putString("message_name",item.contact_name)
                    putString("message_image_link",item.imageLink)
                }

                val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                    putExtra("COMMAND", "MESSAGE")
                }
                itemView.context.startActivity(gotoOtherActivity)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile : ImageView = itemView.findViewById(R.id.imgProfile)
        val tvFullName : TextView = itemView.findViewById(R.id.tvFullName)
        val tvMessage : TextView = itemView.findViewById(R.id.tvMessage)
        val lnRowChat : LinearLayout = itemView.findViewById(R.id.lnRowChat)
    }
}