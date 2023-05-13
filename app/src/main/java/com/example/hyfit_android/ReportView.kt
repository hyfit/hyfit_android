package com.example.hyfit_android

import android.graphics.Bitmap

interface ReportView {
    fun onReportSuccess(totaltime: List<Float>, pace: List<Float>,distance: List<Float>, rate:List<Float>, gname:List<String>)
    fun onReportFailure(code:Int)
}