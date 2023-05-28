package com.example.hyfit_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.FindPasswordReq
import com.example.hyfit_android.databinding.FragmentReportBinding
import com.example.hyfit_android.report.ActivityDashboardActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
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
    lateinit var hemail:String

    private lateinit var mChart: BarChart
    private lateinit var pieChart1 : PieChart
    private lateinit var pieChart2 : PieChart
    private lateinit var linechart:LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        progressBar = binding.progressBar
        progressBar.bringToFront()
        val sharedPrefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        hemail = sharedPrefs.getString("getEmail", "")!!
        report(hemail)

        binding.total1.bringToFront()
        binding.total2.bringToFront()
        binding.total3.bringToFront()
        binding.sex1.bringToFront()
        binding.sex2.bringToFront()
        binding.sex3.bringToFront()
        binding.age1.bringToFront()
        binding.age2.bringToFront()
        binding.age3.bringToFront()
        binding.chart.bringToFront()

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


        binding.sex1.setOnClickListener {
            binding.totalranking.visibility=View.GONE
            binding.genderranking.visibility=View.VISIBLE
        }
        binding.age1.setOnClickListener {
            binding.totalranking.visibility=View.GONE
            binding.ageranking.visibility=View.VISIBLE
        }

        binding.total2.setOnClickListener {
            binding.genderranking.visibility=View.GONE
            binding.totalranking.visibility=View.VISIBLE
        }
        binding.age2.setOnClickListener {
            binding.genderranking.visibility=View.GONE
            binding.ageranking.visibility=View.VISIBLE
        }
        binding.total3.setOnClickListener {
            binding.ageranking.visibility=View.GONE
            binding.totalranking.visibility=View.VISIBLE
        }
        binding.sex3.setOnClickListener {
            binding.ageranking.visibility=View.GONE
            binding.genderranking.visibility=View.VISIBLE
        }

        binding.pieChart1.setOnClickListener {
            binding.modalContainer1.bringToFront()
            binding.modalContainer1.visibility=View.VISIBLE
        }
        binding.click1.setOnClickListener {
            binding.modalContainer1.bringToFront()
            binding.modalContainer1.visibility = View.VISIBLE
        }
        binding.click2.setOnClickListener {
            binding.modalContainer2.bringToFront()
            binding.modalContainer2.visibility=View.VISIBLE
        }

        binding.goToActivityBtn.setOnClickListener{
            val intent = Intent(activity, ActivityDashboardActivity::class.java)
            startActivity(intent)

        }



        val modalLayout1: ConstraintLayout = binding.modalContainer1
        // 모달 레이아웃이 표시된 후에 close 요소에 접근
        val closeTextView1: TextView = modalLayout1.findViewById(R.id.close)
        val modalLayout2: ConstraintLayout = binding.modalContainer2
        // 모달 레이아웃이 표시된 후에 close 요소에 접근
        val closeTextView2: TextView = modalLayout2.findViewById(R.id.close)
        // close 요소에 반응을 추가 (예: 클릭 시 모달 닫기)
        closeTextView1.setOnClickListener {
            // 모달 닫기 동작을 여기에 작성
            binding.modalContainer1.visibility = View.GONE
        }
        closeTextView2.setOnClickListener {
            // 모달 닫기 동작을 여기에 작성
            binding.modalContainer2.visibility = View.GONE
        }








            ///////////////////PIECHART

        //declare a function for pie chart activity
        pieChart1=binding.pieChart1
        pieChart2=binding.pieChart2
        linechart=binding.lineChart
        return binding.root
    }

    private fun setUpLineChart(weight: List<Double>, bodydate: List<String>, line_chart:LineChart){
        val entries: MutableList<Entry> = ArrayList()
        val valued=weight.reversed()
        for (i in valued.indices) {
            entries.add(Entry(i.toFloat(), valued[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "Weight Changes")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.time)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
        line_chart.description.isEnabled = false

        //****
        // Controlling X axis
        val xAxis = line_chart.xAxis
        // Set the xAxis position to bottom. Default is top
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        val months =bodydate
        val arrayList: ArrayList<String> = ArrayList(months)
        arrayList.add(0,"")

        arrayList.add("")

        val formatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < arrayList.size-1) {
                    arrayList[index]
                } else {
                    ""
                }
            }
        }

        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = formatter
        xAxis.textSize=5f

        //***
        // Controlling right side of y axis
        val yAxisRight = line_chart.axisRight
        yAxisRight.isEnabled = false

        //***
        // Controlling left side of y axis
        val yAxisLeft = line_chart.axisLeft
        //yAxisLeft.granularity = 1f 원래 잇던거 내가 죽임
        yAxisLeft.axisLineColor = Color.TRANSPARENT

        // Setting Data
        val data = LineData(dataSet)
        line_chart.data = data
        line_chart.invalidate()
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

        val set2 = BarDataSet(barTwo, "avg pace")
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
        pie.isDrawHoleEnabled = true //원래true
        pie.legend.isEnabled = false
        pie.description.isEnabled = true
        progressBar.visibility=View.GONE
    }

    private fun measurement(weight:Double, height:Double){
        binding.weightnum.text=weight.toString()
        binding.heightnum.text=height.toString()
        val bmi=weight/((height/100.0)*(height/100.0))
        binding.bminum.text=("%.1f".format(bmi).toDouble()).toString()
    }
    private fun changesetting(recentweight:Double,changeweight:Double,goalweight:Double){
        if(changeweight>0){
            binding.changedweightnum.text="+" + changeweight.toInt().toString()
        }
        else{
            binding.changedweightnum.text=changeweight.toInt().toString()
        }
        val untilgoal=(recentweight-goalweight).toInt()
        if(untilgoal<=0) {
            binding.untilgoalnum.text="0"
        }
        else{
            binding.untilgoalnum.text="+"+ untilgoal
        }
        binding.goalweightnum.text=goalweight.toInt().toString()
    }

    private fun report(email:String?){
        val rpService = ReportRetrofitService()
        rpService.setReportView(this)
        binding.progressBar.visibility=View.VISIBLE
        rpService.report(email)

    }

    private fun setTotalRanking(ranking:List<RankInfo>, myrank:Int,reqDist:Double){
        val rankingemail= arrayListOf<String>("")
        val rankingdist= arrayListOf<String>("")
        for (rankInfo in ranking) {
            rankingdist.add((rankInfo.distance).toInt().toString())
            rankingemail.add(rankInfo.email)
        }
        binding.best11.text=rankingemail[1]
        binding.best12.text=rankingemail[2]
        binding.best13.text=rankingemail[3]
        binding.best14.text=rankingemail[4]
        binding.best15.text=rankingemail[5]
        binding.myranknum1.text=(myrank.toString() + ".")
        binding.mynum1.text=reqDist.toInt().toString()

        binding.best11num.text=rankingdist[1]
        binding.best12num.text=rankingdist[2]
        binding.best13num.text=rankingdist[3]
        binding.best14num.text=rankingdist[4]
        binding.best15num.text=rankingdist[5]

    }
    private fun setGenderRanking(ranking:List<RankInfo>, myrank:Int,reqDist:Double){
        val rankingemail= arrayListOf<String>("")
        val rankingdist= arrayListOf<String>("")
        for (rankInfo in ranking) {
            rankingdist.add((rankInfo.distance).toInt().toString())
            rankingemail.add(rankInfo.email)
        }
        binding.best21.text=rankingemail[1]
        binding.best22.text=rankingemail[2]
        binding.best23.text=rankingemail[3]
        binding.best24.text=rankingemail[4]
        binding.best25.text=rankingemail[5]
        binding.myranknum2.text=(myrank.toString() + ".")
        binding.mynum2.text=reqDist.toInt().toString()

        binding.best21num.text=rankingdist[1]
        binding.best22num.text=rankingdist[2]
        binding.best23num.text=rankingdist[3]
        binding.best24num.text=rankingdist[4]
        binding.best25num.text=rankingdist[5]

    }
    private fun setAgeRanking(ranking:List<RankInfo>, myrank:Int,reqDist:Double){
        val rankingemail= arrayListOf<String>("")
        val rankingdist= arrayListOf<String>("")
        for (rankInfo in ranking) {
            rankingdist.add((rankInfo.distance).toInt().toString())
            rankingemail.add(rankInfo.email)
        }
        binding.best31.text=rankingemail[1]
        binding.best32.text=rankingemail[2]
        binding.best33.text=rankingemail[3]
        binding.best34.text=rankingemail[4]
        binding.best35.text=rankingemail[5]
        binding.myranknum3.text=(myrank.toString() + ".")
        binding.mynum3.text=reqDist.toInt().toString()

        binding.best31num.text=rankingdist[1]
        binding.best32num.text=rankingdist[2]
        binding.best33num.text=rankingdist[3]
        binding.best34num.text=rankingdist[4]
        binding.best35num.text=rankingdist[5]

    }



    override fun onReportSuccess(weight:List<Double>,height:List<Double>,bodydate: List<String>,goal_weight:Double,totaltime: List<Float>, pace: List<Float>,distance: List<Float>, rate:List<Float>,
                                 gname:List<String>,goalnum:List<Int>,predictdays:List<Int>,ranking:List<RankInfo>,requestedRank:Int, genderRanking:List<RankInfo>, genderRequestRanking:Int, ageRanking:List<RankInfo>, ageRequestedRank:Int, requestedDist:Double) {
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
            linechart=binding.lineChart
            Log.d("ReportSuccess", "good!")
            setUpBarchart(time,apace,dist)
            setUpSelectionPieChart(pieChart1,rate1,1)
            setUpSelectionPieChart(pieChart2,rate2,2)
            binding.pie1name.bringToFront()
            binding.pie2name.bringToFront()
            binding.pie1name.text=gname1
            binding.pie2name.text=gname2
            setUpLineChart(weight,bodydate,linechart)


            val sharedPreferences = requireActivity().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val email = sharedPreferences.getString("email", "")
            //weight추가
            val recentweight:Double=weight[0]
            val recentheight:Double=height[0]
            measurement(recentweight,recentheight)
            val changeweight:Double=weight[0]-weight[1]
            changesetting(recentweight,changeweight,goal_weight)

            ////////ranking
            //all ranking
            binding.myid.text=email
            binding.myid.bringToFront()
            setTotalRanking(ranking,requestedRank,requestedDist)
            setGenderRanking(genderRanking,genderRequestRanking,requestedDist)
            setAgeRanking(ageRanking,ageRequestedRank,requestedDist)

            ///////modal page
            val modalLayout1: ConstraintLayout = binding.modalContainer1
            // 모달 레이아웃이 표시된 후에 close 요소에 접근
            val predictTextView1: TextView = modalLayout1.findViewById(R.id.predictday)
            val groupTextView1: TextView = modalLayout1.findViewById(R.id.peoplenum)

            val modalLayout2: ConstraintLayout = binding.modalContainer2
            val predictTextView2: TextView = modalLayout2.findViewById(R.id.predictday)
            val groupTextView2: TextView = modalLayout2.findViewById(R.id.peoplenum)
            // close 요소에 반응을 추가 (예: 클릭 시 모달 닫기)
            predictTextView1.text="       "+goalnum[0].toString()
            groupTextView1.text="        "+predictdays[0].toString()
            predictTextView2.text="       "+goalnum[1].toString()
            groupTextView2.text="        "+predictdays[1].toString()



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