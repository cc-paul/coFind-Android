package com.jmr.data

data class ApplicantResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: ApplicantResponseData
)

data class ApplicantResponseData(
    val rows_returned: Int,
    val applicants: List<Applicant>
)

data class Applicant(
    val id: Int,
    val jobID: Int,
    val applicantID: Int,
    val fullName: String,
    val resumeLink: String,
    val imageLink: String,
    val dateCreated: String,
    val status: String
)