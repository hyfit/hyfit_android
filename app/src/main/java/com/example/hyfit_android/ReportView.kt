package com.example.hyfit_android

import android.graphics.Bitmap

interface ReportView {
    fun onReportSuccess(totaltime: List<Float>, pace: List<Float>,distance: List<Float>)
    fun onReportFailure(code:Int)
}