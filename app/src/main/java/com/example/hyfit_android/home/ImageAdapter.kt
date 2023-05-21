package com.example.hyfit_android.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hyfit_android.goal.PlaceImage

class ImageAdapter(fragmentActivity: Fragment, val list:ArrayList<PlaceImage>)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> GoalRecFragment(list[0].src.toString(), list[0].name.toString(), list[0].placeId?.toLong() ?: 0)
            1 ->GoalRecFragment(list[1].src.toString(), list[1].name.toString(), list[1].placeId?.toLong() ?: 0)
            2 ->GoalRecFragment(list[2].src.toString(), list[2].name.toString(), list[2].placeId?.toLong() ?: 0)
            3 -> GoalRecFragment(list[3].src.toString(), list[3].name.toString(), list[3].placeId?.toLong() ?: 0)
            4 ->GoalRecFragment(list[4].src.toString(), list[4].name.toString(), list[4].placeId?.toLong() ?: 0)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}