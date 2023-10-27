package com.jmr.cofindjobsearch.recycleview

data class JobData(
    var id: Int = 0,
    var jobTitle: String = "",
    var requirements: String = "",
    var address: String = "",
    var postedAgo: String = "",
    var jobStatus: String = "",
    var countApplied: Int = 0,
    var completionID: Int = 0,
    var createdBy: Int = 0,
    var applicantID: Int = 0,
    var recruitersName: String = "",
    var applicantsName: String = ""
)