package com.example.hyfit_android.exercise.exerciseWith

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentTypeSelectWithBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import com.example.hyfit_android.goal.Goal
import com.example.hyfit_android.home.GoalSelectFragment
import com.example.hyfit_android.home.GoalSelectFragment2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypeSelectWithFragment : DialogFragment(){
    private lateinit var binding: FragmentTypeSelectWithBinding
    private val goalSelectModal = GoalSelectFragment2()
    private val followingSelectFragment = SelectFollowingFragment()
    private var isSelected: String? = null;
    private var goalId = -1
    private lateinit var mountainList : ArrayList<Goal>
    private lateinit var buildingList : ArrayList<Goal>

    private var myEmail = ""
    private var myNickName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTypeSelectWithBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mainActivity = activity as MainActivity
        val running = binding.runningBtn
        val walking = binding.walkingBtn
        val climbing = binding.climbingBtn
        val stair = binding.stairBtn
        val bundle = arguments
        myEmail = bundle?.getString("MyEmail").toString()
        myNickName = bundle?.getString("MyNickName").toString()
        mountainList = bundle?.getSerializable("mountain") as ArrayList<Goal>
        buildingList = bundle.getSerializable("building") as ArrayList<Goal>

        // X버튼
        binding.selectTypeClose.setOnClickListener{
            dismiss()
        }

        // running click
        running.setOnClickListener{
            if(isSelected == "running"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_select))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = "running"
            }
        }

        // walking click
        walking.setOnClickListener{
            if(isSelected == "walking"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_select))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = "walking"
            }
        }

        // climbing click
        climbing.setOnClickListener{
            if(isSelected == "climbing"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_select))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = "climbing"
            }
        }

        // stair
        stair.setOnClickListener{
            if(isSelected == "stair"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                stair.setBackgroundColor(resources.getColor(R.color.type_select))
                isSelected = "stair"
            }
        }

        // type select 버튼
        binding.typeBtn.setOnClickListener{
            if(isSelected == null){
                Toast.makeText(requireContext(), "You need to select a type.", Toast.LENGTH_SHORT).show()
            }
            else if(isSelected.equals("climbing") || isSelected.equals("stairs")){
                // 목표 선택으로 넘기기
                val bundle = Bundle().apply {
                    putString("type",isSelected)
                    putString("MyEmail",myEmail)
                    putString("MyNickName",myNickName)
                    putSerializable("building", buildingList)
                    putSerializable("mountain", mountainList)
                }
                goalSelectModal.arguments = bundle
                goalSelectModal.show(parentFragmentManager, "goalSelect")
                dismiss()
            }
            else {
                // 친구 선택으로 넘기기
                val bundle = Bundle().apply{
                    putString("type",isSelected)
                    putString("MyEmail",myEmail)
                    putString("MyNickName",myNickName)
                }
                followingSelectFragment.arguments = bundle
                followingSelectFragment.show(parentFragmentManager, "followSelect")
                dismiss()
                }

            }


        return binding.root
    }


}