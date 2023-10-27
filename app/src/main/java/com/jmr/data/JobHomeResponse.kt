package com.jmr.data

data class JobHomeResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: JobList
)

data class JobList(
    val rows_returned: Int,
    val jobs: List<JobDetails>
)

data class JobDetails(
    val id: Int,
    val jobTitle: String,
    val description: String,
    val requirementsList: String,
    val jobType: String,
    val additionalInfo: String,
    val salary: String,
    val imageList: String,
    val status: String,
    val dateCreated: String,
    val f_dateCreated: String,
    val createdBy: Int,
    val fullName: String,
    val imageLink: String,
    val enableApplyButton: Int,
    val countStars: Int
)