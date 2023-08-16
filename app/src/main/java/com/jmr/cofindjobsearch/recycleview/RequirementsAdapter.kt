package com.jmr.cofindjobsearch.recycleview

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.services.Utils

class RequirementsAdapter(var items: ArrayList<RequirementsData>,private val recyclerView: RecyclerView) : RecyclerView.Adapter<RequirementsAdapter.ViewHolder>() {
    var rowCount:Int = 0

    fun removeItem(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_req,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = items[position]
        val Utils = Utils()

        holder.apply {
//            imgDelete.setOnClickListener {
//                if (rowCount != 1) {
//                    removeItem(position)
//                } else {
//                    Utils.showToastMessage(holder.itemView.context,"Please provide at least one field to input requirements")
//                }
//            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val etRequirements : EditText = itemView.findViewById(R.id.etRequirements)
        //val imgDelete : ImageView = itemView.findViewById(R.id.imgDelete)
    }
}