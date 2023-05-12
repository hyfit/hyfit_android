package com.example.hyfit_android.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentGoalModalDeleteBinding
import com.example.hyfit_android.databinding.FragmentGoalSelect2Binding
import com.example.hyfit_android.databinding.FragmentGoalSelectBinding
import com.example.hyfit_android.goal.*

class GoalSelectFragment2 : DialogFragment(),GetMountainView, OnGoalClickListener, GetBuildingView{
    private lateinit var binding: FragmentGoalSelect2Binding
    private lateinit var goalSelectRVAdaptor: GoalSelectRVAdaptor
    private lateinit var mountainList : ArrayList<Goal>
    private lateinit var buildingList : ArrayList<Goal>
    private var isSelected = 0
    private var goalId = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentGoalSelect2Binding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // X버튼
        binding.selectGoalClose.setOnClickListener{
            dismiss()
        }

        val bundle = arguments
        mountainList = bundle?.getSerializable("mountain") as ArrayList<Goal>
        buildingList = bundle?.getSerializable("building") as ArrayList<Goal>

        initSearchRecyclerView(mountainList)

        binding.goalBuildingBtn.setOnClickListener{
            initSearchRecyclerView(buildingList)
        }

        binding.goalMountainBtn.setOnClickListener{
            initSearchRecyclerView(mountainList)
        }
        // 구분선
        binding.selectGoalList.addItemDecoration(DividerItemDecoration(context, 1))

        // 다음 버튼
        binding.selectBtn.setOnClickListener{
            val typeSelectModal = TypeSelectFragment()
            val bundle = Bundle().apply {
                putInt("goalId",goalId)
            }
            typeSelectModal.arguments = bundle
            typeSelectModal.dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            typeSelectModal.show(parentFragmentManager, "typeSelect")
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
        binding.selectBtn.text =  "Select Goal"
        isSelected  = 1
    }

    override fun onItemNonSelected() {
        isSelected = 0
        goalId = -1
        binding.selectBtn.text = "Next Step"
    }
}