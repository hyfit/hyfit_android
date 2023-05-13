package com.example.hyfit_android.goal.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.databinding.ActivityExerciseBinding
import com.example.hyfit_android.databinding.ActivityExerciseDataBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetExerciseByGoalView
import kotlin.properties.Delegates

class ExerciseDataActivity : AppCompatActivity() , GetExerciseByGoalView{

    private lateinit var binding: ActivityExerciseDataBinding
    private lateinit var header : String
    private var goalId by Delegates.notNull<Long>()
    private lateinit var exerciseDataRVAdaptor : ExerciseDataRVAdaptor

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
        getExerciseByGoal()

    }

    private fun getExerciseByGoal(){
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseByGoalView(this)
        exerciseService.getExerciseByGoal(goalId)

    }

    private fun initRecyclerView(result : List<Exercise>){
        exerciseDataRVAdaptor = ExerciseDataRVAdaptor(this, result)
        binding.myExerciseList.adapter = exerciseDataRVAdaptor
        binding.myExerciseList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    override fun onGetExerciseByGoalSuccess(result: List<Exercise>) {
        initRecyclerView(result)
    }

    override fun onGetExerciseByGoalFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }
}