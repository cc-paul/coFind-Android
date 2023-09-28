package com.jmr.cofindjobsearch.recycleview

data class JobData(
    var id: Int = 0,
    var jobTitle: String = "",
    var requirements: String = "",
    var address: String = "",
    var postedAgo: String = "",
    var jobStatus: String = "",
    var countApplied: Int = 0
)