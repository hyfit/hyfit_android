package com.example.hyfit_android.goal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyfit_android.databinding.FragmentGoalBinding
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass.
 * Use the [GoalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalFragment : Fragment() , GetGoalView, GetDoneGoalView, OnGoalChangeListener{
    lateinit var binding: FragmentGoalBinding
    private var gson:Gson = Gson()
    private lateinit var goalDetailRVAdapter: GoalDetailRVAdapter
    private lateinit var goalList : ArrayList<Goal>
//    private val fragment3 = GoalModalFragment3()
//    private val deleteFragment = GoalModalDelete()
//    private val fragment3 = GoalModalFragment3().apply { onChangeListener = this@GoalFragment }
    private val deleteFragment = GoalModalDelete().apply { listener = this@GoalFragment }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(inflater, container, false)
        // 페이지 들어오자마자 getGoal
        getGoalProgress()
//        getGoalDone()
//        binding.goalLatest.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val dialogButton = binding.goalPlus
        dialogButton.setOnClickListener {
            val dialogFragment = GoalModalFragment()
            dialogFragment.show(parentFragmentManager, "dialog1")
        }

        binding.goalDone.setOnClickListener{
            getGoalDone()
        }
        binding.goalInProgress.setOnClickListener{
            getGoalProgress()
        }
        return binding.root
    }

//    private fun initProgressRecyclerView(progressGoalList:ArrayList<Goal>){
//        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), progressGoalList,this)
//        binding.myGoalList.adapter = goalDetailRVAdapter
//        binding.myGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
//    }
//    private fun initDoneRecyclerView(DoneGoalList:ArrayList<Goal>){
//        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), DoneGoalList,this)
//        binding.myDoneGoalList.adapter = goalDetailRVAdapter
//        binding.myDoneGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
//    }

    private fun initRecyclerView(result : ArrayList<Goal>){
        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), result,this)
        binding.myGoalList.adapter = goalDetailRVAdapter
        binding.myGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }

    private fun getGoalProgress(){
        val jwt = getJwt()
        val goalService = GoalService()
        goalService.setGetGoalView(this)
         goalService.getGoalProgress(jwt!!)

    }
    private fun getGoalDone(){
        val jwt = getJwt()
        val goalDoneJson = arguments?.getString("goal")
        val goalDone = gson.fromJson(goalDoneJson, Goal::class.java)
        val goalService = GoalService()
        goalService.setGetDoneGoalView(this)
        goalService.getGoalDone(jwt!!)
    }
    override fun onGetGoalSuccess(result: ArrayList<Goal>) {
//        initProgressRecyclerView(result)
        goalList = result
        initRecyclerView(result)
    }

    override fun onGetGoalFailure(code: Int, msg: String) {
    }

    override fun onGetDoneGoalSuccess(result: ArrayList<Goal>) {
//        initDoneRecyclerView(result)
        goalList = result
        initRecyclerView(result)
    }

    override fun onGetDoneGoalFailure(code: Int, msg: String) {

    }

    override fun onItemClick(data: Goal) {
        Log.d("result",data.toString())
        val bundle = Bundle().apply {

            data.goalId?.let { putLong("goalId", it.toLong()) }
            putString("goalTitle",data.place)
            putString("goalRate",data.rate.toString())
        }
        deleteFragment.arguments = bundle
        deleteFragment.show(parentFragmentManager, "dialog_delete")
    }

    override fun onItemChange() {
        Toast.makeText(requireContext(), "delete!!!", Toast.LENGTH_SHORT).show()
        getGoalProgress()
//        goalDetailRVAdapter.notifyDataSetChanged()
//        goalDetailRVAdapter.updateData()
    }

}