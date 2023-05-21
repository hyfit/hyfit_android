package com.example.hyfit_android.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.hyfit_android.databinding.ActivityClimbingResultBinding

class ClimbingResultActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityClimbingResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingResultBinding.inflate(layoutInflater)
        viewPager = binding.viewPager
        var fragment1 = ClimbingResultFragment1()
        var fragment2 = ClimbingResultFragment2()
        var fragments = ArrayList<Fragment>()
        fragments.add(fragment1)
        fragments.add(fragment2)

        var adaptor = ViewPagerAdapter(this,fragments)
        viewPager.adapter = adaptor

        binding.indicator.setViewPager2(viewPager)
        setContentView(binding.root)




    }
}