package com.example.hyfit_android.goal
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.GoalDetailListBinding
import com.example.hyfit_android.databinding.GoalSearchListBinding

class GoalSearchRVAdapter(val context: Context, val result: ArrayList<Place>, val listener: OnItemClickListener): RecyclerView.Adapter<GoalSearchRVAdapter.ViewHolder>() {

    val fragment = GoalModalFragment3()
    private var selectPlaceIndex = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalSearchRVAdapter.ViewHolder {
        val binding: GoalSearchListBinding =
            GoalSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: GoalSearchRVAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(result[position])
        val detailBtn = holder.binding.placeDetail
        val linearLayout = holder.binding.placeDetailLayout
        val parentLayout = holder.binding.goalSearchListLayout
        detailBtn.setOnClickListener{
            if (linearLayout.visibility == View.GONE) {
                linearLayout.visibility = View.VISIBLE
            } else {
                linearLayout.visibility = View.GONE
            }
        }

        holder.binding.goalPlaceTitle.setOnClickListener {
            // 새로운 아이템을 선택할 경우, 이전에 선택된 아이템의 선택을 해제하고 현재 아이템을 선택한다
            if (selectPlaceIndex != -1) {
                notifyItemChanged(selectPlaceIndex)
            }
            if (selectPlaceIndex != holder.bindingAdapterPosition) {
                selectPlaceIndex = holder.bindingAdapterPosition
                parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.main))
                listener.onItemClick(result[position])
            } else {
                selectPlaceIndex = -1
            }
        }
//            Toast.makeText(context, "Already selected", Toast.LENGTH_SHORT).show()

    }

    // 전체 리사이클러뷰의 갯수
    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("ResourceAsColor")
    inner class ViewHolder(val binding: GoalSearchListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.goalSearchListLayout.setOnClickListener {

            }
        }
        @SuppressLint("ResourceAsColor")
        fun bind(searchList : Place) {
            val feet = searchList.altitude?.toDouble()?.times(3.28084)
            binding.goalPlaceTitle.text= searchList.name
            binding.meterContent.text = searchList.altitude + "m"
            if (feet != null) {
                binding.feetContent.text = (Math.round(feet * 10) / 10.0).toString() + "ft"
            }
            binding.placeLocationContent.text = searchList.location
            // 선택된 아이템인 경우, 배경색을 변경한다
            if (bindingAdapterPosition == selectPlaceIndex) {
                binding.goalSearchListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.main))
            } else {
                binding.goalSearchListLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.place_list_color))
            }
        }
    }
}

