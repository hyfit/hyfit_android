package com.example.hyfit_android.goal.info

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.hyfit_android.databinding.ActivityClimbingInfoBinding
import com.example.hyfit_android.databinding.ActivityClimbingResultBinding
import com.example.hyfit_android.exercise.ClimbingResultFragment1
import com.example.hyfit_android.exercise.ClimbingResultFragment2
import com.example.hyfit_android.exercise.ViewPagerAdapter

class ClimbingInfoActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityClimbingInfoBinding
    var exerciseId = 0L

    var distance = 0.0
    var locationList = ArrayList<String>()
    var peakAlt = ""
    var totalTime = 0L
    var increase = 0L
    var date = ""
    var type = ""
    var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingInfoBinding.inflate(layoutInflater)
        viewPager = binding.viewPager
        exerciseId = intent.getLongExtra("exerciseId", 0L)
        distance = (intent.getDoubleExtra("distance", 0.0))
        val distanceResult = String.format("%.2f", (Math.round(distance * 100000.0) / 100000.0))

        // 경로 지정
        locationList = intent.getStringArrayListExtra("locationList") as ArrayList<String>
        date = intent.getStringExtra("date").toString()
        type = intent.getStringExtra("type").toString()
        title = "$date / $type"


        // 최고 고도
//        peakAlt = intent.getStringExtra("peakAlt").toString()
//        increase = intent.getLongExtra("increase", 0L)

        // 시간
//        totalTime = intent.getStringExtra("totalTime")!!.toLong()

        var fragment1 = ClimbingInfoFragment1()
        var fragment2 = ClimbingInfoFragment2()
        var fragments = ArrayList<Fragment>()
        fragments.add(fragment1)
        fragments.add(fragment2)

        var adaptor = ViewPagerAdapter2(this, fragments)
        viewPager.adapter = adaptor

        binding.indicator.setViewPager2(viewPager)
        setContentView(binding.root)


    }
}