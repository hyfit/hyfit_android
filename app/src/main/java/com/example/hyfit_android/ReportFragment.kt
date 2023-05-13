package com.example.hyfit_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
        progressBar.bringToFront()
        val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val temail = sharedPrefs.getString("getEmail", "")
        report(temail)


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







        ///////////////////PIECHART

        //declare a function for pie chart activity
        pieChart1=binding.pieChart1
        pieChart2=binding.pieChart2
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
        progressBar.visibility=View.GONE

    }
    private fun setUpSelectionPieChart(pie:PieChart, rate:Float, cd:Int) {

        //Create a dataset

        val dataArray = ArrayList<PieEntry>()
        dataArray.add(PieEntry(rate))
        dataArray.add(PieEntry(100f-rate))
        val dataSet = PieDataSet(dataArray, "")
        dataSet.valueTextSize=12f
        dataSet.valueTextColor=Color.WHITE

        //Color set for the chart
        val colorSet = java.util.ArrayList<Int>()
        if(cd==1){
            colorSet.add(Color.rgb(164,189,245))
            colorSet.add(Color.rgb(198,198,198))
        }
        else{
            colorSet.add(Color.rgb(246,224,145))
            colorSet.add(Color.rgb(194,194,194))
        }
        dataSet.setColors(colorSet)

        val data = PieData(dataSet)


        //chart description
        pie.description.text = ""
//        if(pie.description.text.length>10) {
//            pie.description.textSize=10f
//            pie.description.setPosition(310f,40f)
//        }
//        else{
//            pie.description.textSize = 20f
//            pie.description.setPosition(290f,40f)
//        }
         //이게 딱 중간에 들어오는듯

        //Chart data and other styling
        pie.data = data
        pie.centerTextRadiusPercent = 0f
        pie.isDrawHoleEnabled = true
        pie.legend.isEnabled = false
        pie.description.isEnabled = true
        progressBar.visibility=View.GONE
    }

    private fun report(email:String?){
        val rpService = ReportRetrofitService()
        rpService.setReportView(this)
        binding.progressBar.visibility=View.VISIBLE
        rpService.report(email)

    }



    override fun onReportSuccess(totaltime: List<Float>, pace: List<Float>,distance: List<Float>, rate:List<Float>, gname:List<String>) {
        if(distance!=null){

            Log.d("GetreportSuccess", "cong")
            val time: ArrayList<Float> = ArrayList(totaltime)
            val apace : ArrayList<Float> = ArrayList(pace)
            val dist : ArrayList<Float> = ArrayList(distance)
            val rate1:Float = rate[0]
            val rate2:Float= rate[1]
            val gname1:String=gname[0]
            val gname2:String=gname[1]
            pieChart1=binding.pieChart1
            pieChart2=binding.pieChart2
            Log.d("ReportSuccess", "good!")
            setUpBarchart(time,apace,dist)
            setUpSelectionPieChart(pieChart1,rate1,1)
            setUpSelectionPieChart(pieChart2,rate2,2)
            binding.pie1name.bringToFront()
            binding.pie2name.bringToFront()
            binding.pie1name.text=gname1
            binding.pie2name.text=gname2

            executeAfterDelay(3000) {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    override fun onReportFailure(code: Int) {
        Log.d("GetreportFail", code.toString())
    }
    fun executeAfterDelay(delayMillis: Long, code: () -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(code, delayMillis)
    }

}