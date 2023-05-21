package com.example.hyfit_android.goal.info

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentClimbingInfo1Binding
import com.example.hyfit_android.databinding.FragmentClimbingInfo2Binding
import com.example.hyfit_android.databinding.FragmentClimbingResult2Binding
import com.example.hyfit_android.exercise.ClimbingResultActivity
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetExerciseView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.properties.Delegates

class ClimbingInfoFragment2 : Fragment() , GetExerciseView {
    private lateinit var binding: FragmentClimbingInfo2Binding
    private lateinit var barChart: BarChart
    private var  totalTime by Delegates.notNull<Long>()
    private var xList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClimbingInfo2Binding.inflate(inflater, container, false)
        val climbingInfoActivity = activity as ClimbingInfoActivity
        val locationList = climbingInfoActivity.locationList
        var index = 0
        var currentTime = 0
        val entries = mutableListOf<BarEntry>()
        getExercise(climbingInfoActivity.exerciseId)
        binding.exerciseInfoText.text = climbingInfoActivity.title
        Log.d("THISISRESULTLIST",locationList.toString())
        if (locationList != null) {
            for (location in locationList) {
                val latLngArr = location.split(",")
                if(latLngArr[3] == "0.0") continue
                else {
                    val xTime = formatXtime(currentTime)
                    xList.add(xTime)
                    entries.add(BarEntry(index.toFloat(),latLngArr[3].toFloat()))
                    currentTime += 30
                    index++
                }
            }
        }

        barChart = binding.stairBarChart

        val dataSet = BarDataSet(entries, "Increase value")
        val data = BarData(dataSet)

        // x축
        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xList) // locationList is the list of locations you are using to create the BarChart

        // y축
        val yAxis = barChart.axisLeft
        barChart.axisRight.isEnabled = false
        yAxis.setDrawGridLines(false)
        yAxis.axisMinimum = 0f

        barChart.data = data
        dataSet.color = R.color.main_color
        barChart.description.isEnabled = false  // description label 제거
        barChart.setFitBars(true)
        barChart.invalidate()


        binding.prevBtn.setOnClickListener{
            climbingInfoActivity.onBackPressed()
        }

        return binding.root
    }
    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun launchMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }
    fun formatXtime(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return when {
            hours > 0 -> String.format("%dh %02dm", hours, minutes)
            minutes > 0 -> String.format("%dm %02ds", minutes, seconds)
            else -> String.format("%ds", seconds)
        }
    }
    private fun getExercise(exerciseId : Long){
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseView(this)
        exerciseService.getExercise(exerciseId)
    }

    override fun onGetExerciseViewSuccess(result: Exercise) {

        // 시간 변경
        binding.result2Time.text = result.totalTime?.let { formatTime(it) }
        //상승
        binding.exerciseDistanceText.text=result.increase+ "m"
        // 최고
        binding.resultPeakAlt.text = result.peakAlt + "m"
    }

    override fun onGetExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}