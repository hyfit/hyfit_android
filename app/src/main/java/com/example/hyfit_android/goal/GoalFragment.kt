package com.example.hyfit_android.goal

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class GoalFragment : Fragment() , GetGoalView, GetDoneGoalView{
    lateinit var binding: FragmentGoalBinding
    private var gson:Gson = Gson()
    private lateinit var goalDetailRVAdapter: GoalDetailRVAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalBinding.inflate(inflater, container, false)
        // 페이지 들어오자마자 getGoal
        getGoalProgress()
        getGoalDone()
//        binding.goalLatest.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val dialogButton = binding.goalPlus
        dialogButton.setOnClickListener {
            val dialogFragment = GoalModalFragment()
            dialogFragment.show(parentFragmentManager, "dialog")
        }
        return binding.root
    }

    private fun initProgressRecyclerView(progressGoalList:ArrayList<Goal>){
        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), progressGoalList)
        binding.myGoalList.adapter = goalDetailRVAdapter
        binding.myGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }
    private fun initDoneRecyclerView(DoneGoalList:ArrayList<Goal>){
        goalDetailRVAdapter = GoalDetailRVAdapter(requireContext(), DoneGoalList)
        binding.myDoneGoalList.adapter = goalDetailRVAdapter
        binding.myDoneGoalList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }



    private fun getJwt():String?{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    private fun getGoalProgress(){
        val jwt = getJwt()
        val goalProgressJson = arguments?.getString("goal")
        val goalProgress = gson.fromJson(goalProgressJson, Goal::class.java)
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
        initProgressRecyclerView(result)

    }

    override fun onGetGoalFailure(code: Int, msg: String) {
    }

    override fun onGetDoneGoalSuccess(result: ArrayList<Goal>) {
        initDoneRecyclerView(result)
    }

    override fun onGetDoneGoalFailure(code: Int, msg: String) {

    }


}