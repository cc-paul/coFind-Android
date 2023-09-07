package com.jmr.cofindjobsearch.recycleview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.Utils
import kotlin.math.ln

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

            lnApply.apply {
                if (item.createdBy == SharedHelper.getInt("user_id")) {
                    isEnabled = false
                    alpha = 0.5f
                } else {
                    if (item.enableApplyButton == 1) {
                        isEnabled = true
                        alpha = 1f
                    } else {
                        isEnabled = false
                        alpha = 0.5f
                    }
                }
            }
            lnApply.visibility = View.INVISIBLE

            lnApply.setOnClickListener {
                SharedHelper.apply {
                    putInt("receiver_id",item.createdBy)
                    putInt("job_id",item.jobID)
                    putString("person_link",item.imageLink)
                    putString("person_name",item.fullName)
                    putString("person_ago",item.datePosted)
                }

                val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                    putExtra("COMMAND", "JOB_DETAILS")
                }
                itemView.context.startActivity(gotoOtherActivity)
            }

            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_jobs_home, popupMenu.menu)

                popupMenu.apply {
                    menu.apply {
                        findItem(R.id.menu_contact).isEnabled = item.createdBy != SharedHelper.getInt("user_id")

                        if (item.createdBy == SharedHelper.getInt("user_id")) {
                            findItem(R.id.menu_apply).isEnabled = false
                        } else {
                            findItem(R.id.menu_apply).isEnabled = item.enableApplyButton == 1
                        }
                    }

                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_contact -> {
                                SharedHelper.apply {
                                    putInt("receiver_id",item.createdBy)
                                    putString("message_name",item.fullName)
                                    putString("message_image_link",item.imageLink)
                                }

                                val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                                    putExtra("COMMAND", "MESSAGE")
                                }
                                itemView.context.startActivity(gotoOtherActivity)
                                true
                            }
                            R.id.menu_apply -> {
                                lnApply.performClick()
                                true
                            }
                            else -> false
                        }
                    }

                    // Show the PopupMenu
                    show()
                    true
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

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
        val imgMore: ImageView = itemView.findViewById(R.id.imgMore)
    }
}