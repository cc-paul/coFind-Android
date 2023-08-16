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

class JobAdapter(var items: ArrayList<JobData>) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_jobs,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val Utils = Utils()

        holder.apply {
            var countRequirements = item.requirements.split("~").size

            tvJobTitle.text = item.jobTitle
            tvRequirements.text = "$countRequirements Requirement(s)"
            tvAddress.text = item.address
            tvPostedAgo.text = item.postedAgo

            imgEdit.setOnClickListener {
                SharedHelper.apply {
                    putInt("job_id",item.id)
                    putString("job_command","UPDATE_JOB")
                }

                val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                    putExtra("COMMAND", "NEW_JOB")
                }
                itemView.context.startActivity(gotoOtherActivity)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvJobTitle : TextView = itemView.findViewById(R.id.tvJobTitle)
        val tvRequirements : TextView = itemView.findViewById(R.id.tvRequirements)
        val tvAddress : TextView = itemView.findViewById(R.id.tvAddress)
        val tvPostedAgo : TextView = itemView.findViewById(R.id.tvPostedAgo)
        val imgEdit : ImageView = itemView.findViewById(R.id.imgEdit)
    }
}