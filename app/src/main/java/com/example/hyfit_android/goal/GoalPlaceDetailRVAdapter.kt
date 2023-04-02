package com.example.hyfit_android.goal
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModal3Binding
import com.example.hyfit_android.databinding.GoalDetailListBinding
import com.example.hyfit_android.databinding.GoalSearchListBinding

class GoalPlaceDetailRVAdapter(val context: Context, val result: ArrayList<Place>,val listener: OnItemClickListener): RecyclerView.Adapter<GoalPlaceDetailRVAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalPlaceDetailRVAdapter.ViewHolder {
        val binding: FragmentGoalModal3Binding =
            FragmentGoalModal3Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: GoalPlaceDetailRVAdapter.ViewHolder, position: Int) {
        holder.bind(result[position])

    }

    // 전체 리사이클러뷰의 갯수
    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(val binding: FragmentGoalModal3Binding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind(searchList : Place) {
            val feet = searchList.altitude?.toDouble()?.times(3.28084)
//            binding.meterContent.text = searchList.altitude + "m"
//            if (feet != null) {
//                binding.feetContent.text = (Math.round(feet * 10) / 10).toString() + "ft"
//            }
//            binding.placeLocationContent.text = "LOC: " + searchList.location

        }

    }
}

