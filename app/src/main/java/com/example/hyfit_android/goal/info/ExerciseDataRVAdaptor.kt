package com.example.hyfit_android.goal.info

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.databinding.GoalExerciseDataListBinding
import com.example.hyfit_android.exercise.Exercise
class ExerciseDataRVAdaptor(val context: Context, val result: List<Exercise>, val listener: OnExerciseClickListener): RecyclerView.Adapter<ExerciseDataRVAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDataRVAdaptor.ViewHolder {
        val binding: GoalExerciseDataListBinding =
            GoalExerciseDataListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ExerciseDataRVAdaptor.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.exerciseInfoBtn.setOnClickListener{
            listener.onExerciseClick(result[position])
        }
          holder.bind(result[position])
    }
    override fun getItemCount(): Int {
        return result.size
    }
    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(val binding: GoalExerciseDataListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(result: Exercise) {
            binding.exerciseDate.text = result.start?.substringBefore("T") ?: null
            binding.exerciseType.text  = result.type
        }
    }
}