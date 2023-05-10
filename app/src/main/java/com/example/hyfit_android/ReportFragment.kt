package com.example.hyfit_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.databinding.FragmentReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer


/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment(), ReportView{
    lateinit var binding: FragmentReportBinding
    lateinit var progressBar: ProgressBar

    private lateinit var mChart: BarChart
    private lateinit var pieChart1 : PieChart
    private lateinit var pieChart2 : PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        progressBar = binding.progressBar
        binding.change.setOnClickListener {
            binding.layoutdashboard.visibility = View.GONE
            binding.layoutchange.visibility = View.VISIBLE
        }
        binding.ranking.setOnClickListener {
            binding.layoutdashboard.visibility = View.GONE
            binding.layoutranking.visibility = View.VISIBLE
        }
        binding.dashboard2.setOnClickListener {
            binding.layoutchange.visibility = View.GONE
            binding.layoutdashboard.visibility = View.VISIBLE
        }
        binding.ranking2.setOnClickListener {
            binding.layoutchange.visibility = View.GONE
            binding.layoutranking.visibility = View.VISIBLE
        }
        binding.dashboard3.setOnClickListener {
            binding.layoutranking.visibility = View.GONE
            binding.layoutdashboard.visibility = View.VISIBLE
        }
        binding.change3.setOnClickListener {
            binding.layoutranking.visibility = View.GONE
            binding.layoutchange.visibility = View.VISIBLE
        }
        binding.clickhere.setOnClickListener {
            report("dkdud203@naver.com")
            //여기 Getuser로 해서 이메일 따와야함
            //그리고 자동으로 버튼눌러지게 fragment들어와서 1초후에 버튼 누르게..하면 동기화 잘 될라나 ㅎ?
            
        }





        ///////////////////PIECHART

        //declare a function for pie chart activity
        pieChart1=binding.pieChart1
        pieChart2=binding.pieChart2
        setUpSelectionPieChart(pieChart1)
        setUpSelectionPieChart(pieChart2)
        return binding.root
    }

    private fun setUpBarchart(time:ArrayList<Float>, pace:ArrayList<Float>, distance:ArrayList<Float>){
        mChart = binding.chart
        mChart.setDrawBarShadow(false)
        mChart.description.isEnabled = false
        mChart.setPinchZoom(false)
        mChart.setDrawGridBackground(false)
// empty labels so that the names are spread evenly
        val labels = arrayOf("", "5Days ago", "4Days ago", "3Days ago", "Yesterday", "Today", "")
        val xAxis = mChart.xAxis
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK)
        xAxis.textSize = 10f
        xAxis.axisLineColor = Color.WHITE
        xAxis.axisMinimum = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        val leftAxis = mChart.axisLeft
        leftAxis.setTextColor(Color.BLACK)
        leftAxis.textSize = 12f
        leftAxis.axisLineColor = Color.WHITE
        leftAxis.setDrawGridLines(true)
        leftAxis.granularity = 2f
        leftAxis.labelCount = 5//원래8
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)

        mChart.axisRight.isEnabled = false
        mChart.legend.isEnabled = false
        mChart.setBackgroundColor(Color.WHITE)

        val valOne = distance
        val valTwo = pace
        val valThree = time

        val barOne = ArrayList<BarEntry>()
        val barTwo = ArrayList<BarEntry>()
        val barThree = ArrayList<BarEntry>()

        for (i in valOne.indices) {
            barOne.add(BarEntry(i.toFloat(), valOne[i]))
            barTwo.add(BarEntry(i.toFloat(), valTwo[i]))
            barThree.add(BarEntry(i.toFloat(), valThree[i]))
        }

        val set1 = BarDataSet(barOne, "Distance")
        set1.color =ContextCompat.getColor(requireContext(),R.color.distance)

        val set2 = BarDataSet(barTwo, "kcal")
        set2.color = ContextCompat.getColor(requireContext(),R.color.kcal)
        val set3 = BarDataSet(barThree, "Time")
        set3.color = ContextCompat.getColor(requireContext(),R.color.time)

        //차트에서 바 혹은 점 등의 요소를 클릭하거나 터치했을 때 하이라이트를 적용할지 여부를 설정하는 옵션
        set1.isHighlightEnabled = false
        set2.isHighlightEnabled = false
        set3.isHighlightEnabled = false
        set1.setDrawValues(true)
        set2.setDrawValues(true)
        set3.setDrawValues(true)

        val legend = mChart.legend
        legend.isEnabled = true
        legend.textSize = 10f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.setDrawInside(false)


        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val data = BarData(dataSets)
        val groupSpace = 0.39f
        val barSpace = 0.05f
        val barWidth = 0.15f
// (barSpace + barWidth) * 2 + groupSpace = 1
        data.barWidth = barWidth
// so that the entire chart is shown when scrolled from right to left
        xAxis.axisMaximum = labels.size - 1.1f
        mChart.setFitBars(true)
        mChart.data = data
        mChart.setScaleEnabled(true)
        mChart.setVisibleXRangeMaximum(6f)
        mChart.groupBars(1f, groupSpace, barSpace)
        mChart.invalidate()

    }
    private fun setUpSelectionPieChart(pie:PieChart) {

        //Create a dataset
        val dataArray = ArrayList<PieEntry>()
        dataArray.add(PieEntry(38f))
        dataArray.add(PieEntry(14f))
        dataArray.add(PieEntry(14f))
        dataArray.add(PieEntry(34f))
        val dataSet = PieDataSet(dataArray, "")
        dataSet.valueTextSize=15f
        dataSet.valueTextColor=Color.WHITE

        //Color set for the chart
        val colorSet = java.util.ArrayList<Int>()
        colorSet.add(Color.rgb(255,107,107))  //red
        colorSet.add(Color.rgb(173,232,244))  // blue
        colorSet.add(Color.rgb(216,243,220))  // green
        colorSet.add(Color.rgb(255,230,109))  // Yellow
        dataSet.setColors(colorSet)

        val data = PieData(dataSet)


        //chart description
        pie.description.text = "Pie chart"
        pie.description.textSize = 20f
        pie.description.setPosition(300f,40f) //이게 딱 중간에 들어오는듯

        //Chart data and other styling
        pie.data = data
        pie.centerTextRadiusPercent = 0f
        pie.isDrawHoleEnabled = true
        pie.legend.isEnabled = false
        pie.description.isEnabled = true
    }

    private fun report(email:String?){
        val rpService = ReportRetrofitService()
        rpService.setReportView(this)
        rpService.report(email)
    }

    override fun onReportSuccess(totaltime: List<Float>, pace: List<Float>,distance: List<Float>) {
        if(distance!=null){

            Log.d("GetreportSuccess", "cong")
            val time: ArrayList<Float> = ArrayList(totaltime)
            val apace : ArrayList<Float> = ArrayList(pace)
            val dist : ArrayList<Float> = ArrayList(distance)
            Log.d("ReportSuccess", "good!")
            setUpBarchart(time,apace,dist)
            progressBar.visibility=ProgressBar.GONE
        }
    }

    override fun onReportFailure(code: Int) {
        Log.d("GetreportFail", code.toString())
    }

}