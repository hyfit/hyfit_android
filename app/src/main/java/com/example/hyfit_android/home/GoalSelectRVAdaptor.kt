package com.example.hyfit_android.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.GoalSelectListBinding
import com.example.hyfit_android.goal.Goal
import com.example.hyfit_android.goal.OnItemClickListener

class GoalSelectRVAdaptor(val context: Context, val result: ArrayList<Goal>, val listener: OnGoalClickListener): RecyclerView.Adapter<GoalSelectRVAdaptor.ViewHolder>() {

    private var selectGoalIndex = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalSelectRVAdaptor.ViewHolder {
        val binding: GoalSelectListBinding =
            GoalSelectListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: GoalSelectRVAdaptor.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(result[position])
        val layout = holder.binding.goalSelectListLayout
        val title = holder.binding.goalSelectTitle

        title.setOnClickListener{
            if (selectGoalIndex != -1) {
                notifyItemChanged(selectGoalIndex)
            }
            if (selectGoalIndex != holder.bindingAdapterPosition) {
                selectGoalIndex = holder.bindingAdapterPosition
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_select))
                listener.onItemClick(result[position])
            } else {
                selectGoalIndex = -1
                listener.onItemNonSelected()
            }
        }
    }
    override fun getItemCount(): Int {
        return result.size
    }


    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(val binding: GoalSelectListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(result : Goal) {
            binding.goalSelectTitle.text = "${result.place} (${result.rate}%)"
//            binding.goalPercentageContent.text = "${result.rate}%"
//            binding.goalSelectGainContent.text = "${result.gain}m"
            if (bindingAdapterPosition == selectGoalIndex) {
                binding.goalSelectListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_select))
            } else {
                binding.goalSelectListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_unselect))
            }
        }
    }
}