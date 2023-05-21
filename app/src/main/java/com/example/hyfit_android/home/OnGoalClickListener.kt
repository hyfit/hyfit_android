package com.example.hyfit_android.home

import com.example.hyfit_android.goal.Goal
import com.example.hyfit_android.goal.Place

interface OnGoalClickListener {
    fun onItemClick(data: Goal)

    fun onItemNonSelected()
}