package com.example.hyfit_android.goal.info

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.GoalExerciseDataListBinding
import com.example.hyfit_android.databinding.GoalSelectListBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.goal.Goal
import com.example.hyfit_android.goal.OnItemClickListener

class ExerciseDataRVAdaptor(val context: Context, val result: List<Exercise>): RecyclerView.Adapter<ExerciseDataRVAdaptor.ViewHolder>() {
    private val itemsPerPage = 6 // 한 페이지에 보여줄 아이템의 수
    private var currentPage = 0 // 현재 페이지 번호
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDataRVAdaptor.ViewHolder {
        val binding: GoalExerciseDataListBinding =
            GoalExerciseDataListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ExerciseDataRVAdaptor.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
      //   holder.bind(result[position])
        val itemPosition = position + currentPage * itemsPerPage
        if (itemPosition < result.size) {
            holder.bind(result[itemPosition])
        }

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
    fun nextPage() {
        currentPage++
        notifyDataSetChanged()
    }

    fun prevPage() {
        currentPage--
        notifyDataSetChanged()
    }
}