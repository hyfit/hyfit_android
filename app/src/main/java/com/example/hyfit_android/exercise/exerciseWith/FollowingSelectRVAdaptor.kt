package com.example.hyfit_android.exercise.exerciseWith

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FollowingSelectListBinding
import com.example.hyfit_android.home.OnGoalClickListener

class FollowingSelectRVAdaptor(val context: Context, val result: List<String>, val listener: FollowingClickListener): RecyclerView.Adapter<FollowingSelectRVAdaptor.ViewHolder>() {

    private var selectFollowingIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingSelectRVAdaptor.ViewHolder {
        val binding: FollowingSelectListBinding =
            FollowingSelectListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FollowingSelectRVAdaptor.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(result[position])
        val layout = holder.binding.followingSelectListLayout
        val title = holder.binding.followingSelectTitle

        title.setOnClickListener{
            if (selectFollowingIndex != -1) {
                notifyItemChanged(selectFollowingIndex)
            }
            if (selectFollowingIndex != holder.bindingAdapterPosition) {
                selectFollowingIndex = holder.bindingAdapterPosition
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_select))
                // listner click
                listener.onFollowClick(result[position])
            } else {
                selectFollowingIndex = -1
                listener.onFollowNonClicked()
            }
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(val binding: FollowingSelectListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(result : String) {
            var nickName = result.split(",")[1]
            var email =result.split(",")[0]

            binding.followingSelectTitle.text = "$nickName ($email)"
            if (bindingAdapterPosition == selectFollowingIndex) {
                binding.followingSelectListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_select))
            } else {
                binding.followingSelectListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.type_unselect))
            }

        }
    }
}