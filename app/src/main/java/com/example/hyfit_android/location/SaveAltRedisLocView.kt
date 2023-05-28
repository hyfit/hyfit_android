package com.example.hyfit_android.location

interface SaveAltRedisLocView {
    fun onSaveAltRedisLocSuccess(result: String)
    fun onSaveAltRedisLocFailure(code:Int, msg:String)
}