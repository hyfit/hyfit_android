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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs
import kotlin.properties.Delegates

class StairResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStairResultBinding
    private lateinit var barChart: BarChart
    private var  totalTime by Delegates.notNull<Long>()
    private var xList = mutableListOf<String>()
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationList = intent.getStringArrayListExtra("locationList")
        var index = 0
        var currentTime = 0
        val entries = mutableListOf<BarEntry>()
          var minAltitude = Float.MAX_VALUE
        Log.d("THISISRESULTLIST",locationList.toString())
          if (locationList != null) {
              for (location in locationList) {
                  val latLngArr = location.split(",")
                  if(latLngArr[2] == "0.0") continue
                  else {
                      val xTime = formatXtime(currentTime)
                      xList.add(xTime)
                      Log.d("THISISALT",latLngArr[2])
                      if (latLngArr[2].toFloat() < minAltitude) {
                          minAltitude = latLngArr[2].toFloat()
                      }
                      entries.add(BarEntry(index.toFloat(),latLngArr[2].toFloat()))
                      currentTime += 10
                      index++
                  }
              }
          }
          binding = ActivityStairResultBinding.inflate(layoutInflater)
          setContentView(binding.root)
          barChart = binding.stairBarChart

          val dataSet = BarDataSet(entries, "Altitude change")
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
          yAxis.axisMinimum =minAltitude - 1

          barChart.data = data
          dataSet.color = R.color.main_color
          barChart.description.isEnabled = false  // description label 제거
          barChart.setFitBars(true)
          barChart.invalidate()

          // 상승값
        val distance:Double= (intent.getDoubleExtra("increaseValue", 0.0))
        val distanceResult = String.format("%.2f", distance)
        binding.exerciseDistanceText.text= distanceResult+ "m"

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