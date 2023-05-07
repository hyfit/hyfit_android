package com.example.hyfit_android

import android.graphics.Bitmap

interface ReportView {
    fun onReportSuccess(code:Bitmap)
    fun onReportFailure(code:Int)
}