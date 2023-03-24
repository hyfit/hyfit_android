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
import com.example.hyfit_android.databinding.GoalSearchListBinding

class GoalSearchRVAdapter(val context: Context, val result: ArrayList<Place>): RecyclerView.Adapter<GoalSearchRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalSearchRVAdapter.ViewHolder {
        val binding: GoalSearchListBinding =
            GoalSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: GoalSearchRVAdapter.ViewHolder, position: Int) {
        holder.bind(result[position])
    }

    // 전체 리사이클러뷰의 갯수
    override fun getItemCount(): Int {
        return result.size
    }

    inner class ViewHolder(val binding: GoalSearchListBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(searchList : Place) {
            binding.goalPlaceTitle.text= searchList.name
//            binding.goalPlaceAltitude.text = searchList.altitude + "m"
        }
    }
}

