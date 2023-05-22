package com.example.hyfit_android.location

interface SaveAltRedisLocView {
    fun onSaveAltRedisLocSuccess(result: List<String>)
    fun onSaveAltRedisLocFailure(code:Int, msg:String)
}