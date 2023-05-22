package com.example.hyfit_android.exercise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityStairResultBinding
import com.example.hyfit_android.databinding.FragmentClimbingResult1Binding
import com.example.hyfit_android.databinding.FragmentClimbingResult2Binding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClimbingResultFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClimbingResultFragment2() : Fragment() {
    private lateinit var binding: FragmentClimbingResult2Binding
    private lateinit var barChart: BarChart
    private var  totalTime by Delegates.notNull<Long>()
    private var xList = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentClimbingResult2Binding.inflate(inflater, container, false)
        val climbingResultActivity = activity as ClimbingResultActivity
        val locationList = climbingResultActivity.locationList
        var index = 0
        var currentTime = 0
        val entries = mutableListOf<BarEntry>()

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

        // 상승값
        val increaseResult = String.format("%.2f", climbingResultActivity.increase.toDouble())
        binding.exerciseDistanceText.text=increaseResult+ "m"

        // 시간
        binding.result2Time.text = formatTime(climbingResultActivity.totalTime)

        // 최고
        binding.resultPeakAlt.text = climbingResultActivity.peakAlt + "m"

        // 메인 이동
        binding.goToMain.setOnClickListener{
            launchMainActivity()
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


}