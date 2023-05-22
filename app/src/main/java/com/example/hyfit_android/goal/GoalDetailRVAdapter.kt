package com.example.hyfit_android.goal
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.GoalDetailListBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GoalDetailRVAdapter(val context: Context, val result: ArrayList<Goal>, val listener: OnGoalChangeListener, val clickListener: OnGoalClickListener): RecyclerView.Adapter<GoalDetailRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalDetailRVAdapter.ViewHolder {
        // rv_item의 textView 불러오기

        val binding: GoalDetailListBinding = GoalDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: GoalDetailRVAdapter.ViewHolder, position: Int) {

        holder.binding.goalDeleteButton.setOnClickListener{
            listener.onItemClick(result[position])
        }

        // 전체 box 누를때
        holder.binding.goalLayout.setOnClickListener{
            clickListener.onGoalClick(result[position])
        }
        // view binding (adapter에 넣은 데이터를 rm_item에 넣어주는)
        holder.bind(result[position])

    }

    // 전체 리사이클러뷰의 갯수
    override fun getItemCount(): Int {
        return result.size
    }

    inner class ViewHolder(val binding: GoalDetailListBinding) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("ResourceAsColor")
        fun bind(goal: Goal){
            val goalLayout = binding.goalLayout
            val TextView = binding.goalPlaceTitle
            val TextView2 = binding.goalRate
            val progressBar = binding.progressBar
            val progress = (goal.rate?.toDouble())?.toInt()


            if(goal.goalStatus != 0){
                if(goal.type == "mountain"){
                    goalLayout.setBackgroundResource(R.drawable.ic_goal_rectangle_mountain)
                }
                if(goal.type == "building"){
                    goalLayout.setBackgroundResource(com.example.hyfit_android.R.drawable.ic_goal_rectangle_building)
                }
            }
            else {
                goalLayout.setBackgroundResource(com.example.hyfit_android.R.drawable.ic_goal_rectangle_done)
                TextView!!.setPaintFlags(TextView!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                binding.goalDeleteButton.visibility = View.INVISIBLE
            }
            if (progress != null) {
                progressBar.progress = progress
            }
            TextView.text = goal.place
            TextView2.text = goal.rate.toString() + "%"

//            TextView3.text = goal.description
//            val createdAtString = goal.createdAt.toString()
//
//            Textview4.text = createdAtString.substringBefore("T")
        }
    }

}

