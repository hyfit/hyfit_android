package com.example.hyfit_android.goal.info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.databinding.ActivityExerciseBinding
import com.example.hyfit_android.databinding.ActivityExerciseDataBinding
import com.example.hyfit_android.exercise.*
import com.example.hyfit_android.location.GetAllRedisExerciseView
import com.example.hyfit_android.location.LocationService
import java.util.Collections.min
import kotlin.math.min
import kotlin.properties.Delegates

class ExerciseDataActivity : AppCompatActivity() , GetExerciseByGoalView, OnExerciseClickListener,
    GetAllRedisExerciseView {

    private lateinit var binding: ActivityExerciseDataBinding
    private lateinit var header : String
    private var goalId by Delegates.notNull<Long>()
    private lateinit var exerciseDataRVAdaptor : ExerciseDataRVAdaptor

    private lateinit var exerciseList : List<Exercise>

    // 페이지네이션
    private var pageSize = 6
    private var currentPage = 1
    private var listSize by Delegates.notNull<Int>()
    private var totalPage =1

    private var exerciseId = 0L
    private var type = ""
    private var date = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = ActivityExerciseDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // header 변경
        header = intent.getStringExtra("goal_name").toString()
        binding.goalDataHeaderText.text=header

        //goal Id 받기
        goalId =(intent.getIntExtra("goal_id",0)).toLong()

        // 뒤로가기 버튼
        binding.prevBtn.setOnClickListener{
            onBackPressed()
        }

        // 페이지네이션 왼쪽
        binding.exercisePaginationLeft.setOnClickListener{
            if(currentPage == 1){
                Toast.makeText(this, "This is the first page.", Toast.LENGTH_SHORT).show()
            }
            else{
                currentPage -= 1
                binding.exerciseCurrentPage.text="($currentPage/$totalPage)"
                // init
                initRecyclerView(paginationList())
            }
        }

        // 페이지네이션 오른쪽
        binding.exercisePaginationRight.setOnClickListener{
            if(currentPage==totalPage){
                Toast.makeText(this, "This is the last page.", Toast.LENGTH_SHORT).show()
            }
            else{
                currentPage +=1
                binding.exerciseCurrentPage.text="($currentPage/$totalPage)"
                // init
                initRecyclerView(paginationList())
            }
        }
        getExerciseByGoal()

    }

    private fun paginationList() : List<Exercise> {
        val startIndex = (currentPage - 1) * pageSize
        val endIndex = min(currentPage * pageSize, listSize) - 1
        val paginationList = exerciseList.subList(startIndex, endIndex + 1)
        return paginationList
    }

    private fun getExerciseByGoal(){
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseByGoalView(this)
        exerciseService.getExerciseByGoal(goalId)

    }

    private fun initRecyclerView(result : List<Exercise>){
        exerciseDataRVAdaptor = ExerciseDataRVAdaptor(this, result,this)
        binding.myExerciseList.adapter = exerciseDataRVAdaptor
        binding.myExerciseList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    override fun onGetExerciseByGoalSuccess(result: List<Exercise>) {
        exerciseList = result.reversed()
        listSize = result.size
        totalPage = (listSize + pageSize - 1) / pageSize
        if(totalPage == 0) totalPage = 1
        initRecyclerView(paginationList())
        binding.exerciseCurrentPage.text = "(1/$totalPage)"
    }

    override fun onGetExerciseByGoalFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onExerciseClick(data: Exercise) {
        data.exerciseId?.let { getAllRedisExercise(it) }
        exerciseId = data.exerciseId!!
        type = data.type.toString()
        date = (data.start?.substringBefore("T") ?: null) as String
    }
    private fun getAllRedisExercise(exerciseId : Long){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId.toInt())
    }

    override fun onExerciseClicked() {
        TODO("Not yet implemented")
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
        else {
            val intent = Intent(this,ClimbingInfoActivity::class.java)
            intent.putStringArrayListExtra("locationList",result)
            intent.putExtra("exerciseId",exerciseId)
            intent.putExtra("type",type)
            intent.putExtra("date",date)
            startActivity(intent)
        }
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }
}