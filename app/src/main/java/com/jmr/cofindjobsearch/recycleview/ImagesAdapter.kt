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

class ImagesAdapter(var items: ArrayList<ImagesData>,var job: Create_Job) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_image,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val Utils = Utils()

        holder.apply {
            Glide.with(itemView.context)
                .load(Uri.parse(item.imageLink))
                .into(imgReq)

            lnDeleteReq.setOnClickListener {
                items.removeAt(position)
                notifyItemRemoved(position)
                job.deleteImageItemByLink(item.imageLink)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgReq : ImageView = itemView.findViewById(R.id.imgReq)
        val lnDeleteReq : LinearLayout = itemView.findViewById(R.id.lnDeleteReq)
    }
}