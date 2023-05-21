package com.example.hyfit_android.home

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModalDeleteBinding
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.exercise.ClimbingActivity
import com.example.hyfit_android.exercise.ExerciseActivity
import com.example.hyfit_android.exercise.StairActivity
import com.example.hyfit_android.goal.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GoalSelectFragment : DialogFragment(),GetMountainView, OnGoalClickListener, GetBuildingView{
    private lateinit var binding: FragmentGoalSelectBinding
    private lateinit var goalSelectRVAdaptor: GoalSelectRVAdaptor
    private lateinit var mountainList : ArrayList<Goal>
    private lateinit var buildingList : ArrayList<Goal>
    private var isSelected = 0
    private var goalId = -1
    private var type = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGoalSelectBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mainActivity = activity as MainActivity
        // X버튼
        binding.selectGoalClose.setOnClickListener{
            dismiss()
        }

        val bundle = arguments
        mountainList = bundle?.getSerializable("mountain") as ArrayList<Goal>
        buildingList = bundle.getSerializable("building") as ArrayList<Goal>

        type = bundle.getString("type").toString()
        Log.d("THISISTYPE",type)

        initSearchRecyclerView(mountainList)

        binding.goalBuildingBtn.setOnClickListener{
            initSearchRecyclerView(buildingList)
        }

        binding.goalMountainBtn.setOnClickListener{
            initSearchRecyclerView(mountainList)
        }

        // 구분선
        binding.selectGoalList.addItemDecoration(DividerItemDecoration(context, 1))

        binding.startWorkout.setOnClickListener{

            if(type== "stair"){

                if(mainActivity.initCode == 800 || mainActivity.initCode == 808 ||mainActivity.initCode == 0 ){
                    activity?.let {
                        val intent = Intent(it, StairActivity::class.java).apply {
                            putExtra("goalId",goalId)
                            putExtra("type",type)
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
                            val intent = Intent(it, StairActivity::class.java).apply {
                                putExtra("goalId",goalId)
                                putExtra("type",type)
                            }

                            it.startActivity(intent)
                        }
                    }
                }
            }
            // climbing
            else {
                if(mainActivity.initCode == 800 || mainActivity.initCode == 808 ||mainActivity.initCode == 0 ){
                    activity?.let {
                        val intent = Intent(it, ClimbingActivity::class.java).apply {
                            putExtra("goalId",goalId)
                            putExtra("type",type)
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
                            val intent = Intent(it, ClimbingActivity::class.java).apply {
                                putExtra("goalId",goalId)
                                putExtra("type",type)
                            }

                            it.startActivity(intent)
                        }
                    }
                }
            }


        }
        return binding.root
    }

    private fun initSearchRecyclerView(GoalList : ArrayList<Goal>){
        goalSelectRVAdaptor = GoalSelectRVAdaptor(requireContext(),GoalList,this)
        binding.selectGoalList.adapter = goalSelectRVAdaptor
        binding.selectGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

//    private fun getMountainProgress(){
//        val jwt: String? = getJwt()
//        val goalService = GoalService()
//        goalService.setGetMountainView(this)
//        goalService.getMountainProgress(jwt!!)
//    }
//
//    private fun getBuildingProgress(){
//        val jwt: String? = getJwt()
//        val goalService = GoalService()
//        goalService.setGetBuildingView(this)
//        goalService.getBuildingProgress(jwt!!)
//    }

    private fun getJwt():String?{
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    override fun onGetMountainSuccess(result: ArrayList<Goal>) {
        initSearchRecyclerView(result)
    }

    override fun onGetMountainFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetBuildingSuccess(result: ArrayList<Goal>) {
        initSearchRecyclerView(result)
    }

    override fun onGetBuildingFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(data: Goal) {
        goalId = data.goalId!!
        isSelected  = 1
    }

    override fun onItemNonSelected() {
        isSelected = 0
        goalId = -1
    }
}