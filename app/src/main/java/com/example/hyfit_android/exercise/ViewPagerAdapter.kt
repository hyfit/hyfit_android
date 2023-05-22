package com.example.hyfit_android.exercise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val fragments : List<Fragment>) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return fragments.size // 페이지 수
    }

    override fun createFragment(position: Int): Fragment {
        // 각 페이지에 해당하는 Fragment 반환
        return when (position) {
            0 -> ClimbingResultFragment1()
            1 -> ClimbingResultFragment2()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}