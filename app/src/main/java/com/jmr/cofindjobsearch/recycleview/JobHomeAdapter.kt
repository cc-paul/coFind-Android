package com.jmr.cofindjobsearch.recycleview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextUtils.split
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

class JobHomeAdapter(var items: ArrayList<JobHomeData>) : RecyclerView.Adapter<JobHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_home_jobs,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val Utils = Utils()

        holder.apply {
            if (item.imageLink != "-") {
                Glide.with(itemView.context)
                    .load(Uri.parse(item.imageLink))
                    .into(imgProfile)
            }

            tvFullName.text = item.fullName
            tvDatePosted.text = item.datePosted
            tvSalary.text = item.salary
            tvJobTitle.text = item.jobTitle
            tvJobType.text = item.jobType
            tvJobDescription.text = item.jobDescription

            lnReqHolder.removeAllViews()

            val requirementsList = item.requirements.split("~")
            for (requirement in requirementsList) {
                val inflater = LayoutInflater.from(itemView.context)
                val newRowView = inflater.inflate(R.layout.row_req_tv, lnReqHolder, false)
                val tvReq = newRowView.findViewById<TextView>(R.id.tvRequirements)
                tvReq.text = requirement
                lnReqHolder.addView(newRowView)
            }

            if (item.createdBy == SharedHelper.getInt("user_id")) {
                lnApply.apply {
                    isEnabled = false
                    alpha = 0.5f
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile : ImageView = itemView.findViewById(R.id.imgProfile)
        val tvFullName : TextView = itemView.findViewById(R.id.tvFullName)
        val tvDatePosted : TextView = itemView.findViewById(R.id.tvDatePosted)
        val tvSalary : TextView = itemView.findViewById(R.id.tvSalary)
        val tvJobTitle : TextView = itemView.findViewById(R.id.tvJobTitle)
        val tvJobType : TextView = itemView.findViewById(R.id.tvJobType)
        val tvJobDescription : TextView = itemView.findViewById(R.id.tvJobDescription)
        val lnReqHolder : LinearLayout = itemView.findViewById(R.id.lnReqHolder)
        val lnApply: LinearLayout = itemView.findViewById(R.id.lnApply)
    }
}