package com.example.hyfit_android

import android.graphics.Bitmap

interface ReportView {
    fun onReportSuccess(weight:List<Double>,height:List<Double>,bodydate: List<String>,goal_weight:Double,totaltime: List<Float>, pace: List<Float>,distance: List<Float>, rate:List<Float>, gname:List<String>,
                        ranking:List<RankInfo>,requestedRank:Int, genderRanking:List<RankInfo>, genderRequestRanking:Int, ageRanking:List<RankInfo>, ageRequestedRank:Int, requestedDist:Double)
    fun onReportFailure(code:Int)
}