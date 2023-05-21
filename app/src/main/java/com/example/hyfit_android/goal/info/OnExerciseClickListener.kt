package com.example.hyfit_android.goal.info

import com.example.hyfit_android.exercise.Exercise

interface OnExerciseClickListener {
    fun onExerciseClick(data: Exercise)

    fun onExerciseClicked()

}