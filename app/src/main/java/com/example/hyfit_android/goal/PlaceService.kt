package com.example.hyfit_android.goal

import android.util.Log
import com.example.hyfit_android.getRetrofit
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceService {

    private lateinit var getPlaceView: GetPlaceView

    fun setGetPlaceView(getPlaceView: GetPlaceView){
        this.getPlaceView = getPlaceView
    }

    fun getGoalAllPlace(type : String, continents : String){
        val placeService = getRetrofit().create(GoalRetrofitInterface::class.java)
        placeService.getGoalPlace(type, continents).enqueue(object: Callback<GetPlaceRes> {
            override fun onResponse(call: Call<GetPlaceRes>, response: Response<GetPlaceRes>) {
                Log.d("GOALPLACE/SUCCESS", response.toString())
                val resp: GetPlaceRes = response.body()!!
                Log.d("GOALPLACE/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getPlaceView.onGetPlaceSuccess(resp.result)
                    else-> getPlaceView.onGetPlaceFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetPlaceRes>, t: Throwable) {
                Log.d("GOALPLACE/FAILURE", t.message.toString())
            }
        })
    }

}