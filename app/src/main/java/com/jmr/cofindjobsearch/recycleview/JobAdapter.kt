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
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmr.cofindjobsearch.OtherActivity
import com.jmr.cofindjobsearch.R
import com.jmr.cofindjobsearch.fragments.Create_Job
import com.jmr.cofindjobsearch.helper.SharedHelper
import com.jmr.cofindjobsearch.services.RestAPIServices
import com.jmr.cofindjobsearch.services.Utils
import com.jmr.data.JobSender

class JobAdapter(var items: ArrayList<JobData>) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {
    private val Utils = Utils()
    private val apiService: RestAPIServices by lazy {
        RestAPIServices()
    }

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
            tvTotalApplicants.text = item.countApplied.toString() + " Applicant(s)"

            when(item.jobStatus) {
                "POSTED" -> {
                    imgMore.visibility = View.VISIBLE
                    tvTotalApplicants.visibility = View.VISIBLE
                }
                "APPLIED" -> {
                    imgMore.visibility = View.INVISIBLE
                    tvTotalApplicants.visibility = View.INVISIBLE
                }
                "ACTIVE" -> {
                    if (item.createdBy == SharedHelper.getInt("user_id")) {
                        imgMore.visibility = View.VISIBLE
                    } else {
                        imgMore.visibility = View.INVISIBLE
                    }
                }
            }

            imgMore.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, it)
                popupMenu.menuInflater.inflate(R.menu.menu_jobs, popupMenu.menu)

                when(item.jobStatus) {
                    "POSTED" -> {
                        popupMenu.menu.apply {
                            findItem(R.id.menu_edit).isVisible = true
                            findItem(R.id.menu_delete).isVisible = true
                            findItem(R.id.menu_view_applicant).isVisible = true
                            findItem(R.id.menu_complete).isVisible = false
                            findItem(R.id.menu_review).isVisible = false
                        }
                    }
                    "ACTIVE" -> {
                        popupMenu.menu.apply {
                            findItem(R.id.menu_edit).isVisible = false
                            findItem(R.id.menu_delete).isVisible = false
                            findItem(R.id.menu_view_applicant).isVisible = true
                            findItem(R.id.menu_complete).isVisible = true
                            findItem(R.id.menu_review).isVisible = false
                        }
                    }
                    "COMPLETED" -> {
                        popupMenu.menu.apply {
                            findItem(R.id.menu_edit).isVisible = false
                            findItem(R.id.menu_delete).isVisible = false
                            findItem(R.id.menu_view_applicant).isVisible = item.createdBy == SharedHelper.getInt("user_id")
                            findItem(R.id.menu_complete).isVisible = false
                            findItem(R.id.menu_review).isVisible = true
                        }
                    }
                }

                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.menu_edit -> {
                            SharedHelper.apply {
                                putInt("job_id",item.id)
                                putString("job_command","UPDATE_JOB")
                            }

                            val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                                putExtra("COMMAND", "NEW_JOB")
                            }
                            itemView.context.startActivity(gotoOtherActivity)
                            true
                        }
                        R.id.menu_delete -> {
                            Utils.showProgress(itemView.context)

                            apiService.deleteJob(item.id) { it ->
                                Utils.showToastMessage(itemView.context, it?.messages?.get(0).toString())
                                Utils.closeProgress()

                                if (it!!.success) {
                                    removeItem(position)
                                }
                            }

                            true
                        }
                        R.id.menu_view_applicant -> {

                            SharedHelper.apply {
                                putInt("job_id",item.id)
                                putString("jobStatus",item.jobStatus)
                            }

                            val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                                putExtra("COMMAND", "VIEW_APPLICANT")
                            }
                            itemView.context.startActivity(gotoOtherActivity)

                            true
                        }
                        R.id.menu_complete -> {
                            Utils.showProgress(itemView.context)

                            val jobInfo = JobSender(
                                command = "CHANGE_STATUS",
                                id = item.completionID,
                                status = "Completed"
                            )

                            apiService.saveJob(jobInfo) {
                                Utils.showToastMessage(itemView.context, it?.messages?.get(0).toString())
                                Utils.closeProgress()

                                if (it!!.success) {
                                    removeItem(position)
                                }
                            }
                            true
                        }
                        R.id.menu_review -> {
                            val gotoOtherActivity = Intent(itemView.context, OtherActivity::class.java).apply {
                                putExtra("COMMAND", "REVIEW")

                                putExtra("JOBID", item.id)
                                putExtra("REVIEWERSNAME", if (item.createdBy == SharedHelper.getInt("user_id")) {
                                    item.applicantsName
                                } else {
                                    item.recruitersName
                                })
                                putExtra("REVIEWERID", if (item.createdBy == SharedHelper.getInt("user_id")) {
                                    item.createdBy
                                } else {
                                    item.applicantID
                                })
                                putExtra("REVIEWEDID", if (item.createdBy == SharedHelper.getInt("user_id")) {
                                    item.applicantID
                                } else {
                                    item.createdBy
                                })
                            }
                            itemView.context.startActivity(gotoOtherActivity)
                            true
                        }
                        else -> false
                    }
                }

                // Show the PopupMenu
                popupMenu.show()
                true
            }
        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvJobTitle : TextView = itemView.findViewById(R.id.tvJobTitle)
        val tvRequirements : TextView = itemView.findViewById(R.id.tvRequirements)
        val tvAddress : TextView = itemView.findViewById(R.id.tvAddress)
        val tvPostedAgo : TextView = itemView.findViewById(R.id.tvPostedAgo)
        val imgMore : ImageView = itemView.findViewById(R.id.imgMore)
        val tvTotalApplicants: TextView = itemView.findViewById(R.id.tvTotalApplicants)
    }
}