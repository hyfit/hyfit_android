package com.example.hyfit_android.goal
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.GoalDetailListBinding
import com.example.hyfit_android.databinding.GoalListBinding

class GoalDetailRVAdapter(val context: Context, val result: ArrayList<Goal>): RecyclerView.Adapter<GoalDetailRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalDetailRVAdapter.ViewHolder {
        // rv_item의 textView 불러오기

        val binding: GoalDetailListBinding = GoalDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: GoalDetailRVAdapter.ViewHolder, position: Int) {

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

            if(goal.goalStatus != 0){
                if(goal.type == "mountain"){
                    goalLayout.setBackgroundResource(R.drawable.ic_goal_rectangle_mountain)
                }
                else if(goal.type == "building"){
                    goalLayout.setBackgroundResource(com.example.hyfit_android.R.drawable.ic_goal_rectangle_building)

                }
            }
            else {
                goalLayout.setBackgroundResource(com.example.hyfit_android.R.drawable.ic_goal_rectangle_done)
                val TextView = binding.goalPlaceTitle
                val TextView2 = binding.goalRate
        
                TextView!!.setPaintFlags(TextView!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                TextView2!!.setPaintFlags(TextView!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            }
            binding.goalPlaceTitle.text = goal.place
            binding.goalRate.text = goal.rate.toString() + "%"
            binding.goalDescription.text = goal.description




        }
    }
}

