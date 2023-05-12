package com.example.hyfit_android.home

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.databinding.FragmentTypeSelectBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import com.example.hyfit_android.exercise.StairActivity
import com.example.hyfit_android.goal.Goal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypeSelectFragment : DialogFragment() {
    private lateinit var binding: FragmentTypeSelectBinding
    private var isSelected: String? = null;
    private var goalId = -1
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentTypeSelectBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mainActivity = activity as MainActivity
        val running = binding.runningBtn
        val walking = binding.walkingBtn
        val climbing = binding.climbingBtn

        val bundle = arguments
        if (bundle != null) {
            goalId = bundle.getInt("goalId")
        }
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
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_select))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = "running"
            }
        }

        // walking click
        walking.setOnClickListener{
            if(isSelected == "walking"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_select))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = "walking"
            }
        }

        // climbing click
        climbing.setOnClickListener{
            if(isSelected == "climbing"){
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_unselect))
                isSelected = null
            }
            else {
                running.setBackgroundColor(resources.getColor(R.color.type_unselect))
                walking.setBackgroundColor(resources.getColor(R.color.type_unselect))
                climbing.setBackgroundColor(resources.getColor(R.color.type_select))
                isSelected = "climbing"
            }
        }


        // type select 버튼
        binding.typeBtn.setOnClickListener{
            if(isSelected == null){
                Toast.makeText(requireContext(), "You need to select a type.", Toast.LENGTH_SHORT).show()
            }
            else {
                if(mainActivity.initCode == 800 || mainActivity.initCode == 808 ||mainActivity.initCode == 0 ){
                    activity?.let {
                        val intent = Intent(it, ExerciseActivity::class.java).apply {
                            putExtra("goalId",goalId)
                            putExtra("type",isSelected)
                        }

                        it.startActivity(intent)
                    }
                }
                else {
                    binding.progressBar.visibility = ProgressBar.VISIBLE
                    lifecycleScope.launch {

                        while(mainActivity.initCode != 800 ) {
                            Log.d("STATUSCODE",mainActivity.initCode.toString())
                            delay(1000)
                        }
                        activity?.let {
                            val intent = Intent(it, ExerciseActivity::class.java).apply {
                                putExtra("goalId",goalId)
                                putExtra("type",isSelected)
                            }

                            it.startActivity(intent)
                        }
                    }
                }

            }
        }

        return binding.root
    }

}