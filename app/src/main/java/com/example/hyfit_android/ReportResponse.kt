package com.example.hyfit_android

import com.google.gson.annotations.SerializedName

//data class ReportResponse(
//    @SerializedName("date") val date: List<String>,
//    @SerializedName("totaltime") val totaltime: List<Float>,
//    @SerializedName("pace") val pace: List<Float>,
//    @SerializedName("distance") val distance: List<Float>,
//    @SerializedName("code") val code:Int,
//    @SerializedName("gname") val gname:List<String>,
//    @SerializedName("rate") val rate:List<Float>
//)
data class ReportResponse(
    @SerializedName("body") val body: BodyData,
    @SerializedName("rank") val rank: RankingData,
    @SerializedName("report") val report: ReportData
)

data class BodyData(
    @SerializedName("bodydate") val bodydate: List<String>,
    @SerializedName("height") val height: List<Double>,
    @SerializedName("weight") val weight: List<Double>,
    @SerializedName("goal_weight") val goal_weight:Double
)
data class RankingData(
    @SerializedName("ranking") val ranking: List<RankInfo>,
    @SerializedName("gender_ranking") val genderRanking: List<RankInfo>,
    @SerializedName("requested_rank") val requestedRank: Int,
    @SerializedName("gender_request_ranking") val genderRequestRanking: Int,
    @SerializedName("age_ranking") val ageRanking: List<RankInfo>,
    @SerializedName("age_requested_rank") val ageRequestedRank: Int,
    @SerializedName("requested_dist") val requestedDist:Double
)
data class RankInfo(
    @SerializedName("distance") val distance: Double,
    @SerializedName("email") val email: String,
    @SerializedName("rank") val rank: Int
)
data class ReportData(
    @SerializedName("code") val code: Int,
    @SerializedName("distance") val distance: List<Float>,
    @SerializedName("exdate") val exdate: List<String>,
    @SerializedName("gname") val gname: List<String>,
    @SerializedName("pace") val pace: List<Float>,
    @SerializedName("rate") val rate: List<Float>,
    @SerializedName("totaltime") val totaltime: List<Float>,
    @SerializedName("weight") val weight: List<Double>,
    @SerializedName("date") val date: List<String>
)