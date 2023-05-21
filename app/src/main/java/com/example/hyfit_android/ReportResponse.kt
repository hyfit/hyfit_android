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
    @SerializedName("report") val report: ReportData
)

data class BodyData(
    @SerializedName("bodydate") val bodydate: List<String>,
    @SerializedName("height") val height: List<Double>,
    @SerializedName("weight") val weight: List<Double>,
    @SerializedName("goal_weight") val goal_weight:Double
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