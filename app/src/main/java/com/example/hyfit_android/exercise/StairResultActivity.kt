package com.example.hyfit_android.exercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityExerciseResultBinding
import com.example.hyfit_android.databinding.ActivityStairResultBinding
import com.example.hyfit_android.home.MainFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.maps.model.LatLng
import kotlin.properties.Delegates

class StairResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStairResultBinding
    private lateinit var barChart: BarChart
    private var  totalTime by Delegates.notNull<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationList = intent.getStringArrayListExtra("locationList")
        var index = 0
        val entries = mutableListOf<BarEntry>()

        if (locationList != null) {
            for (location in locationList) {
                val latLngArr = location.split(",")
                Log.d("THISISALT",(latLngArr[2].toFloat()).toString())
                entries.add(BarEntry(index.toFloat(),latLngArr[2].toFloat()))
                index++
            }
        }
        binding = ActivityStairResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barChart = binding.stairBarChart

        val dataSet = BarDataSet(entries, "Altitude Data")
        val data = BarData(dataSet)

        barChart.data = data
        dataSet.color = R.color.main_color
        barChart.setFitBars(true)
        barChart.invalidate()

        // 상승값
        val distance:Double= (intent.getDoubleExtra("distance", 0.0))
        val distanceResult = String.format("%.2f", distance)
        binding.exerciseDistanceText.text=distanceResult+ "m"

        // 시간
        totalTime = intent.getStringExtra("totalTime")!!.toLong()
        binding.resultTimeText.text = formatTime(totalTime)

        // 메인 이동
        binding.goToMain.setOnClickListener{
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}