package com.example.hyfit_android.report

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.databinding.ActivityDashboardBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetAllExerciseView
import com.example.hyfit_android.goal.info.ClimbingInfoActivity
import com.example.hyfit_android.goal.info.ExerciseDataRVAdaptor
import com.example.hyfit_android.goal.info.OnExerciseClickListener
import com.example.hyfit_android.goal.info.StairInfoActivity
import com.example.hyfit_android.location.GetAllRedisExerciseView
import com.example.hyfit_android.location.LocationService

class ActivityDashboardActivity : AppCompatActivity(),GetAllExerciseView, OnExerciseClickListener,
    GetAllRedisExerciseView {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var exerciseDataRVAdaptor : ExerciseDataRVAdaptor
    private lateinit var exerciseList : List<Exercise>

    private var exerciseId = 0L
    private var type = ""
    private var date = ""
    private var pace = ""
    private var time = 0L
    private var distance = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDashboardBinding.inflate(layoutInflater)
        getAllExercise()

        // 뒤로가기 버튼
        binding.prevBtn.setOnClickListener{
            onBackPressed()
        }

        setContentView(binding.root)
    }

    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun getAllExercise(){
        val jwt = getJwt()
        val exerciseService = ExerciseService()
        exerciseService.setGetAllExerciseView(this)
        exerciseService.getAllExercise(jwt!!)
        binding.progressBar.visibility = View.VISIBLE
    }
    override fun onGetAllExerciseSuccess(result: List<Exercise>) {
        binding.progressBar.visibility = View.GONE
        exerciseList = result
        initRecyclerView(result)
    }

    override fun onGetAllExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    private fun initRecyclerView(result : List<Exercise>){
        exerciseDataRVAdaptor = ExerciseDataRVAdaptor(this, result,this)
        binding.myActivityList.adapter = exerciseDataRVAdaptor
        binding.myActivityList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }
    private fun getAllRedisExercise(exerciseId : Long){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId.toInt())
    }
    override fun onGetAllRedisExerciseSuccess(result: ArrayList<String>) {

        if(type == "stair"){
            val intent= Intent(this, StairInfoActivity::class.java)
            intent.putStringArrayListExtra("locationList",result)
            intent.putExtra("exerciseId",exerciseId)
            intent.putExtra("type",type)
            intent.putExtra("date",date)
            startActivity(intent)
        }
        else if(type =="climbing") {
            val intent = Intent(this, ClimbingInfoActivity::class.java)
            intent.putStringArrayListExtra("locationList",result)
            intent.putExtra("exerciseId",exerciseId)
            intent.putExtra("type",type)
            intent.putExtra("date",date)
            startActivity(intent)
        }
        else{
            val intent = Intent(this,RunningInfoActivity::class.java)
            intent.putStringArrayListExtra("locationList",result)
            intent.putExtra("exerciseId",exerciseId)
            intent.putExtra("type",type)
            intent.putExtra("date",date)
            intent.putExtra("pace",pace)
            intent.putExtra("distance",distance)
            intent.putExtra("time",time)
            startActivity(intent)
        }

    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onExerciseClick(data: Exercise) {
        data.exerciseId?.let { getAllRedisExercise(it) }
        exerciseId = data.exerciseId!!
        type = data.type.toString()
        date = (data.start?.substringBefore("T") ?: null) as String
        pace = data.pace.toString()
        distance = data.distance.toString()
        time = data.totalTime!!
    }

    override fun onExerciseClicked() {
        TODO("Not yet implemented")
    }



}