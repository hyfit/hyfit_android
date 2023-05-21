package com.example.hyfit_android.goal.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityStairInfoBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetExerciseView
import com.example.hyfit_android.location.GetAllRedisExerciseView
import com.example.hyfit_android.location.LocationService
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.properties.Delegates

class StairInfoActivity : AppCompatActivity(),GetAllRedisExerciseView,GetExerciseView {
    private lateinit var binding: ActivityStairInfoBinding
    private var exerciseId by Delegates.notNull<Long>()
    private var  totalTime by Delegates.notNull<Long>()

    // chart
    private lateinit var barChart: BarChart
    private var xList = mutableListOf<String>()
    private var index =0
    private var currentTime = 0
    private var entries =  mutableListOf<BarEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding
        binding = ActivityStairInfoBinding.inflate(layoutInflater)
        barChart = binding.stairBarChart

        val dataSet = BarDataSet(entries, "Increase value")
        val data = BarData(dataSet)

        // 뒤로가기 버튼
        binding.prevBtn.setOnClickListener{
            onBackPressed()
        }

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

        exerciseId = intent.getLongExtra("exerciseId", 0L)

        if(exerciseId!=null){
            getAllRedisExercise()
            getExercise()
        }


        setContentView(binding.root)
    }


    private fun getAllRedisExercise(){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId.toInt())
    }
    override fun onGetAllRedisExerciseSuccess(result: ArrayList<String>) {
        for (location in result) {
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

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


    private fun getExercise(){
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseView(this)
        exerciseService.getExercise(exerciseId)
    }

    override fun onGetExerciseViewSuccess(result: Exercise) {
        binding.exerciseDistanceText.text= result.pace+ "m"
        binding.resultTimeText.text = result.totalTime?.let { formatTime(it) }
        binding.exerciseInfoText.text = "${result.start?.substringBefore("T")} / ${result.type}"

    }

    override fun onGetExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
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

}