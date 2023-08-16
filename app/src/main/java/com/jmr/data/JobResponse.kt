package com.jmr.data

data class JobResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: JobData
)

data class JobData(
    val rows_returned: Int,
    val jobs: List<Job>
)

data class Job(
    val id: Int,
    val jobTitle: String,
    val description: String,
    val requirementsList: String,
    val jobType: String,
    val additionalInfo: String,
    val salary: String,
    val forDiscussion: Int,
    val imageList: String,
    val status: String,
    val createdBy: Int,
    val isActive: Int,
    val dateCreated: String,
    val f_dateCreated: String,
    val address: String
)