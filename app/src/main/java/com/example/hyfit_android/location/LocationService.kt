package com.example.hyfit_android.location

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationService {
    private lateinit var getAllExerciseListView: GetAllExerciseListView
    private lateinit var getRedisExerciseView: GetRedisExerciseView
    private lateinit var saveExerciseLocView: SaveExerciseLocView
    private lateinit var saveExerciseRedisLocView: SaveExerciseRedisLocView
    private lateinit var getAllRedisExerciseView: GetAllRedisExerciseView
    private lateinit var saveAltRedisLocView: SaveAltRedisLocView

    fun setGetAllExerciseListView(getAllExerciseListView: GetAllExerciseListView){
        this.getAllExerciseListView = getAllExerciseListView
    }
    fun setGetRedisExerciseView(getRedisExerciseView: GetRedisExerciseView){
        this.getRedisExerciseView = getRedisExerciseView
    }
    fun setSaveAltRedisView(saveAltRedisLocView: SaveAltRedisLocView){
        this.saveAltRedisLocView = saveAltRedisLocView
    }
    fun setSaveExerciseLocView(saveExerciseLocView: SaveExerciseLocView){
        this.saveExerciseLocView = saveExerciseLocView
    }
    fun setSaveExerciseRedisLocView(saveExerciseRedisLocView: SaveExerciseRedisLocView){
        this.saveExerciseRedisLocView = saveExerciseRedisLocView
    }

    fun setGetAllRedisExerciseView(getAllRedisExerciseView : GetAllRedisExerciseView){
        this.getAllRedisExerciseView = getAllRedisExerciseView
    }

    fun saveExerciseLoc(locationExerciseSaveReq: LocationExerciseSaveReq){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.saveExerciseLoc(locationExerciseSaveReq).enqueue(object: Callback<LocationExerciseRes> {
            override fun onResponse(call: Call<LocationExerciseRes>, response: Response<LocationExerciseRes>) {
                Log.d("LOCATION/SUCCESS", response.toString())
                val resp: LocationExerciseRes = response.body()!!
                Log.d("LOCATION/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> saveExerciseLocView.onSaveExerciseLocSuccess(resp.result)
                    else-> saveExerciseLocView.onSaveExerciseLocFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationExerciseRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

    fun saveExerciseRedisLoc(locationRedisReq: LocationRedisReq){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.saveExerciseRedisLoc(locationRedisReq).enqueue(object: Callback<LocationRedisRes> {
            override fun onResponse(call: Call<LocationRedisRes>, response: Response<LocationRedisRes>) {
                Log.d("LOCATION/SUCCESS", response.toString())
                val resp: LocationRedisRes = response.body()!!
                Log.d("LOCATION/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> saveExerciseRedisLocView.onSaveExerciseRedisLocSuccess(resp.result)
                    else-> saveExerciseRedisLocView.onSaveExerciseRedisLocFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationRedisRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

    fun saveRedisAltExercise(locationAltRedisReq : LocationAltRedisReq){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.saveRedisAltExercise(locationAltRedisReq).enqueue(object: Callback<LocationRedisRes> {
            override fun onResponse(call: Call<LocationRedisRes>, response: Response<LocationRedisRes>) {
                Log.d("LOCATION/SUCCESS", response.toString())
                val resp: LocationRedisRes = response.body()!!
                Log.d("LOCATION/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> saveAltRedisLocView.onSaveAltRedisLocSuccess(resp.result)
                    else-> saveAltRedisLocView.onSaveAltRedisLocFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationRedisRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllExerciseList(id : Int){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.getAllExerciseList(id).enqueue(object: Callback<LocationAllRedisRes> {
            override fun onResponse(call: Call<LocationAllRedisRes>, response: Response<LocationAllRedisRes>) {
                Log.d("LOCATIONGET/SUCCESS", response.toString())
                val resp: LocationAllRedisRes = response.body()!!
                Log.d("LOCATIONGET/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getAllExerciseListView.onGetAllExerciseListSuccess(resp.result)
                    else-> getAllExerciseListView.onGetAllExerciseListFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationAllRedisRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

    fun getRedisExercise(id : Int){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.getRedisExercise(id).enqueue(object: Callback<LocationRedisExerciseRes> {
            override fun onResponse(call: Call<LocationRedisExerciseRes>, response: Response<LocationRedisExerciseRes>) {
                Log.d("LOCATION/SUCCESS", response.toString())
                val resp: LocationRedisExerciseRes = response.body()!!
                Log.d("LOCATION/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getRedisExerciseView.onGetRedisExerciseViewSuccess(resp.result)
                    else-> getRedisExerciseView.onGetRedisExerciseViewFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationRedisExerciseRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

    fun getAllRedisExercise(id : Int){
        val locationService = getRetrofit().create(LocationRetrofitInterface::class.java)
        locationService.getAllRedisExercise(id).enqueue(object: Callback<LocationAllRedisRes> {
            override fun onResponse(call: Call<LocationAllRedisRes>, response: Response<LocationAllRedisRes>) {
                Log.d("LOCATION/SUCCESS", response.toString())
                val resp: LocationAllRedisRes = response.body()!!
                Log.d("LOCATION/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getAllRedisExerciseView.onGetAllRedisExerciseSuccess(resp.result)
                    else-> getAllRedisExerciseView.onGetAllRedisExerciseFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<LocationAllRedisRes>, t: Throwable) {
                Log.d("LOCATION/FAILURE", t.message.toString())
            }
        })
    }

}