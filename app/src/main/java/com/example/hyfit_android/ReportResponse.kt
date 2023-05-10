package com.example.hyfit_android

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("date") val date: List<String>,
    @SerializedName("totaltime") val totaltime: List<Float>,
    @SerializedName("pace") val pace: List<Float>,
    @SerializedName("distance") val distance: List<Float>,
    @SerializedName("code") val code:Int
)