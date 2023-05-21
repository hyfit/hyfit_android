package com.example.hyfit_android.goal

import android.util.Log
import com.example.hyfit_android.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoalService {
    private lateinit var getGoalView: GetGoalView
    private lateinit var getDoneGoalView: GetDoneGoalView
    private lateinit var saveGoalView: SaveGoalView
    private lateinit var deleteGoalView: DeleteGoalView
    private lateinit var getBuildingView: GetBuildingView
    private lateinit var getMountainView: GetMountainView
    private lateinit var modifyGoalView: ModifyGoalView
    private lateinit var getGoalRecView : GetGoalRecView
//    private lateinit var getGoalImageView : GetGoalImageView

    fun setGetGoalView(getGoalView: GetGoalView){
        this.getGoalView = getGoalView
    }

    fun setGetDoneGoalView(getDoneGoalView: GetDoneGoalView){
        this.getDoneGoalView = getDoneGoalView
    }
    fun setSaveGoalView(saveGoalView: SaveGoalView){
        this.saveGoalView = saveGoalView
    }
    fun setDeleteGoalView(deleteGoalView: DeleteGoalView){
        this.deleteGoalView = deleteGoalView
    }

    fun setGetBuildingView(getBuildingView: GetBuildingView){
        this.getBuildingView = getBuildingView
    }
    fun setGetMountainView(getMountainView: GetMountainView){
        this.getMountainView = getMountainView
    }
    fun setModifyGoalView(modifyGoalView: ModifyGoalView){
        this.modifyGoalView = modifyGoalView
    }

    fun setGoalRecView(getGoalRecView: GetGoalRecView){
        this.getGoalRecView = getGoalRecView
    }
//
//    fun setGoalImageView(getGoalImageView: GetGoalImageView){
//        this.getGoalImageView = getGoalImageView
//    }

    fun getGoalProgress(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getGoalProgress(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("PROGRESSGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("PROGRESSGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getGoalView.onGetGoalSuccess(resp.result)
                    else-> getGoalView.onGetGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("PROGRESSGOAL/FAILURE", t.message.toString())
            }
        })
    }

    // get mountain
    fun getMountainProgress(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getMountain(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("MOUNTAINGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("MOUNTAINGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getMountainView.onGetMountainSuccess(resp.result)
                    else-> getMountainView.onGetMountainFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("MOUNTAINGOAL/FAILURE", t.message.toString())
            }
        })
    }

    // get building
    fun getBuildingProgress(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getBuilding(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("BUILDINGGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("BUILDINGGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getBuildingView.onGetBuildingSuccess(resp.result)
                    else-> getBuildingView.onGetBuildingFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("BUILDINGGOAL/FAILURE", t.message.toString())
            }
        })
    }

    // modify building
    fun saveGoal(jwt : String,id:Long,gain:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.modifyGoal(jwt,id,gain).enqueue(object : Callback<SaveGoalRes>{
            override fun onResponse(call : Call<SaveGoalRes>, response : Response<SaveGoalRes>){
                Log.d("MODIFYGOAL/SUCCESS", response.toString())
                val resp: SaveGoalRes = response.body()!!
                Log.d("MODIFYGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> modifyGoalView.onModifyGoalSuccess(resp.result)
                    else-> modifyGoalView.onModifyGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<SaveGoalRes>, t: Throwable) {
                Log.d("MODIFYGOAL/FAILURE", t.message.toString())
            }
        })
    }
    fun getGoalDone(jwt:String){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.getGoalDone(jwt).enqueue(object: Callback<GetGoalRes> {
            override fun onResponse(call: Call<GetGoalRes>, response: Response<GetGoalRes>) {
                Log.d("DONEGOAL/SUCCESS", response.toString())
                val resp: GetGoalRes = response.body()!!
                Log.d("DONEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getDoneGoalView.onGetDoneGoalSuccess(resp.result)
                    else-> getDoneGoalView.onGetDoneGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalRes>, t: Throwable) {
                Log.d("DONEGOAL/FAILURE", t.message.toString())
            }
        })
    }

    fun saveGoal(jwt : String,saveGoalReq: SaveGoalReq){
        val postService = getRetrofit().create(GoalRetrofitInterface::class.java)
        postService.saveGoal(jwt,saveGoalReq).enqueue(object : Callback<SaveGoalRes>{
            override fun onResponse(call : Call<SaveGoalRes>, response : Response<SaveGoalRes>){
                Log.d("SAVEGOAL/SUCCESS", response.toString())
                val resp: SaveGoalRes = response.body()!!
                Log.d("SAVEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> saveGoalView.onSaveGoalSuccess(resp.result)
                    else-> saveGoalView.onSaveGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<SaveGoalRes>, t: Throwable) {
                Log.d("SAVEGOAL/FAILURE", t.message.toString())
            }
        })
    }
    fun deleteGoal(jwt : String,id : Long){
        val goalService = getRetrofit().create(GoalRetrofitInterface::class.java)
        goalService.deleteGoal(jwt,id).enqueue(object : Callback<DeleteGoalRes>{
            override fun onResponse(call : Call<DeleteGoalRes>, response : Response<DeleteGoalRes>){
                Log.d("DELETEGOAL/SUCCESS", response.toString())
                val resp: DeleteGoalRes = response.body()!!
                Log.d("DELETEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> deleteGoalView.onDeleteGoalSuccess(resp.result)
                    else-> deleteGoalView.onDeleteGoalFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<DeleteGoalRes>, t: Throwable) {
                Log.d("DELETEGOAL/FAILURE", t.message.toString())
            }
        })
    }
    // goal rec
    fun getGoalRec(jwt : String){
        val goalService = getRetrofit().create(GoalRetrofitInterface::class.java)
        goalService.getGoalRec(jwt).enqueue(object: Callback<GetGoalImageRes> {
            override fun onResponse(call: Call<GetGoalImageRes>, response: Response<GetGoalImageRes>) {
                Log.d("DONEGOAL/SUCCESS", response.toString())
                val resp: GetGoalImageRes = response.body()!!
                Log.d("DONEGOAL/SUCCESS", resp.toString())
                when(val code = resp.code){
                    1000-> getGoalRecView.onGetGoalRecSuccess(resp.result)
                    else-> getGoalRecView.onGetGoalRecFailure(code, resp.message)
                }
            }
            override fun onFailure(call: Call<GetGoalImageRes>, t: Throwable) {
                Log.d("DONEGOAL/FAILURE", t.message.toString())
            }
        })
    }

//    fun getGoalImage(jwt:String, placeId : Long){
//        val goalService = getRetrofit().create(GoalRetrofitInterface::class.java)
//        goalService.getPlaceImage(jwt,placeId).enqueue(object: Callback<GetGoalImageRes> {
//            override fun onResponse(call: Call<GetGoalImageRes>, response: Response<GetGoalImageRes>) {
//                Log.d("DONEGOAL/SUCCESS", response.toString())
//                val resp: GetGoalImageRes = response.body()!!
//                Log.d("DONEGOAL/SUCCESS", resp.toString())
//                when(val code = resp.code){
//                    1000-> getGoalImageView.onGetGoalImageViewSuccess(resp.result)
//                    else-> getGoalImageView.onGetGoalImageViewFailure(code, resp.message)
//                }
//            }
//            override fun onFailure(call: Call<GetGoalImageRes>, t: Throwable) {
//                Log.d("DONEGOAL/FAILURE", t.message.toString())
//            }
//        })
//    }

    }

