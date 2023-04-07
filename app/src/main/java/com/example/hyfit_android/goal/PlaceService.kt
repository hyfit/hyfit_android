package com.example.hyfit_android.goal

import android.util.Log
import com.example.hyfit_android.getRetrofit
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceService {

    private lateinit var getPlaceView: GetPlaceView
    private lateinit var getPlacePageView: GetPlacePageView

    fun setGetPlaceView(getPlaceView: GetPlaceView){
        this.getPlaceView = getPlaceView
    }

    fun setPlacePageView(getPlacePageView:GetPlacePageView){
        this.getPlacePageView = getPlacePageView
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

    // 페이지 size 가져오기
    fun getPageSize(type : String, continents : String){
        val placeService = getRetrofit().create(GoalRetrofitInterface::class.java)
        placeService.getPageSize(type, continents).enqueue(object: Callback<GetPlacePageRes>{
            override fun onResponse(call: Call<GetPlacePageRes>, response: Response<GetPlacePageRes>) {
                Log.d("PLACEPAGE/SUCCESS", response.toString())
                val resp: GetPlacePageRes = response.body()!!
                Log.d("PLACEPAGE/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getPlacePageView.onGetPlacePageSuccess(resp.result)
                    else-> getPlacePageView.onGetPlacePageFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetPlacePageRes>, t: Throwable) {
                Log.d("PLACEPAGE/FAILURE", t.message.toString())
            }
        })
    }

    fun getPlaceByPage(type : String, continents : String, page:Int){
        val placeService = getRetrofit().create(GoalRetrofitInterface::class.java)
        placeService.getGoalPlacePage(type, continents,page).enqueue(object: Callback<GetPlaceRes> {
            override fun onResponse(call: Call<GetPlaceRes>, response: Response<GetPlaceRes>) {
                Log.d("PLACEBYPAGE/SUCCESS", response.toString())
                val resp: GetPlaceRes = response.body()!!
                Log.d("PLACEBYPAGE/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getPlaceView.onGetPlaceSuccess(resp.result)
                    else-> getPlaceView.onGetPlaceFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetPlaceRes>, t: Throwable) {
                Log.d("PLACEBYPAGE/FAILURE", t.message.toString())
            }
        })
    }
}